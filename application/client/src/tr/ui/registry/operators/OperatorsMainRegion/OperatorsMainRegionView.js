define([
    'jscore/core',
    'text!./OperatorsMainRegion.html',
    'styles!./OperatorsMainRegion.less',
    'styles!../../../../cssPatches/app/MainRegions.less'
], function (core, template, style, stylePatch) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.panesHolder = element.find('.eaTR-OperatorsMainRegion-panesHolder');
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
