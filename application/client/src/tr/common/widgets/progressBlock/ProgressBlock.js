define([
    'jscore/core',
    './ProgressBlockView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({

        View: View,

        onViewReady: function () {
            this.view.afterRender();
        },

        showProgress: function () {
            this.view.getProgressBlock().setModifier('show');
        },

        hideProgress: function () {
            this.view.getProgressBlock().removeModifier('show');
        }

    });

});
