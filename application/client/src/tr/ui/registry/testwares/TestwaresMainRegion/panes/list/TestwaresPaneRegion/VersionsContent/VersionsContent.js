define([
    'jscore/core',
    './VersionsContentView',
    './VersionItem/VersionItem'
], function (core, View, VersionItem) {
    'use strict';

    var VersionsContent = core.Widget.extend({

        View: View,

        init: function () {
            this._versionItems = [];
        },

        onViewReady: function () {
            this.view.afterRender();

            var data = this.options.row.getData();
            this.eventBus = this.options.eventBus;

            for (var i = 0; i < data.versions.length; i++) {
                if (i > 0) {
                    var separator = core.Element.parse('<span> | </span>');
                    this.view.getVersionsHolder().append(separator);
                }
                var versionItem = new VersionItem({
                    version: data.versions[i],
                    id: data.ids[i]
                });
                versionItem.attachTo(this.view.getVersionsHolder());

                this._versionItems.push(versionItem);
            }

            this.eventBus.subscribe(VersionsContent.SELECT_TESTWARE_VERSION, this.onSelectTestwareVersion, this);
        },

        onSelectTestwareVersion: function (testwareId) {
            this.deselectItems();
            if (testwareId) {
                this.selectItem(testwareId);
            }
        },

        deselectItems: function () {
            this._versionItems.forEach(function (versionItem) {
                versionItem.deselect();
            });
        },

        selectItem: function (testwareId) {
            this._versionItems.forEach(function (versionItem) {
                if (testwareId === versionItem.getTestwareId()) {
                    versionItem.select();
                }
            });
        }

    }, {
        SELECT_TESTWARE_VERSION: 'selectTestwareVersion'
    });

    return VersionsContent;

});
