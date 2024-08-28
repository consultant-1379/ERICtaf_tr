/*global define*/
define([
    'jscore/core',
    'text!./ActionLink.html',
    'styles!./_actionLink.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getLink: function () {
            return this.getElement().find('.eaTR-ActionLink-link');
        },

        getIconHolder: function () {
            return this.getElement().find('.eaTR-ActionLink-iconHolder');
        }
    });

});
