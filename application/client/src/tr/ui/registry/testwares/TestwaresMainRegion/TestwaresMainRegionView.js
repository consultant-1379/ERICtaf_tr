define([
    'jscore/core',
    'text!./TestwaresMainRegion.html',
    'styles!./TestwaresMainRegion.less',
    'styles!../../../../cssPatches/app/MainRegions.less'
], function (core, template, style, stylePatch) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.panesHolder = element.find('.eaTR-TestwaresMainRegion-panesHolder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return stylePatch + style;
        },

        getPanesHolder: function () {
            return this.panesHolder;
        }

    });

});
