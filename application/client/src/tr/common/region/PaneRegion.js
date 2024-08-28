define([
    'jscore/core'
], function (core) {
    'use strict';

    return core.Region.extend({

        init: function () {
            this.isShown = false;
        },

        show: function () {
            this.view.getElement().setModifier('show');
            this.onShow.apply(this, arguments);
            this.isShown = true;
        },

        hide: function () {
            this.view.getElement().removeModifier('show');
            this.onHide.apply(this, arguments);
            this.isShown = false;
        },

        redraw: function () {
            this.onRedraw.apply(this, arguments);
        },

        getShown: function () {
            return this.isShown;
        },

        applyViewScreen: function (left, width) {
            this.view.getElement().setStyle('left', left);
            this.view.getElement().setStyle('width', width);
        },

        onShow: function () {
        },
        onHide: function () {
        },
        onRedraw: function () {
        }

    });

});
