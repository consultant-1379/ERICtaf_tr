/*global define*/
define([
    'jscore/core',
    'text!./_progressBlock.html',
    'styles!./_progressBlock.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.progressBlock = element.find('.eaTR-ProgressBlock-progress');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getProgressBlock: function () {
            return this.progressBlock;
        }

    });

});
