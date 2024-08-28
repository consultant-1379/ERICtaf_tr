/*global define*/
define([
    'jscore/core',
    'template!./OwnersWidget.html',
    'styles!./OwnersWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template({template: this.options.template});
        },

        getStyle: function () {
            return styles;
        }

    });

});
