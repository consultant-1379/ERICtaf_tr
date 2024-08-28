define([
    'jscore/core',
    'text!./VersionsContent.html',
    'styles!./VersionsContent.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.versionsList = element.find('.eaTR-VersionsContent-versions');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getVersionsHolder: function () {
            return this.versionsList;
        }

    });

});
