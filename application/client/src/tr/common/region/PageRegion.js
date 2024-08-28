define([
    'jscore/core'
], function (core) {
    'use strict';

    return core.Region.extend({

        init: function () {
            this.isShown = false;
        },

        show: function () {
            this.view.getElement().setModifier('show', 'true', 'eaTR-Content-page');
            this.onShow.apply(this, arguments);
            this.isShown = true;
        },

        hide: function () {
            this.view.getElement().removeModifier('show', 'eaTR-Content-page');
            this.onHide.apply(this, arguments);
            this.isShown = false;
        },

        redraw: function () {
            this.onRedraw.apply(this, arguments);
        },

        getShown: function () {
            return this.isShown;
        },

        onShow: function () {
        },
        onHide: function () {
        },
        onRedraw: function () {
        }

    });

});
