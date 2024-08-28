define([
    'jscore/core',
    './VersionItemView',
    '../../../../../../../../../common/Navigation'
], function (core, View, Navigation) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function () {
            this.isSelected = false;
        },

        onViewReady: function () {
            var version = this.options.version,
                selected = this.options.selected;

            this.id = this.options.id;

            if (selected) {
                this.select();
            }

            this.getElement().setText(version);
            this.getElement().addEventHandler('click', this.onElementClick, this);
        },

        getTestwareId: function () {
            return this.id;
        },

        onElementClick: function (e) {
            e.preventDefault();
            var navigateToUrl = '';
            if (this.isSelected) {
                navigateToUrl = Navigation.getDefaultUrl();
            } else {
                navigateToUrl = Navigation.getTestwareDetailsUrlWithParams(this.id);
            }
            Navigation.navigateTo(navigateToUrl);
        },

        deselect: function () {
            this.isSelected = false;
            this.getElement().removeModifier('selected');
        },

        select: function () {
            this.isSelected = true;
            this.getElement().setModifier('selected');
        }

    });

});
