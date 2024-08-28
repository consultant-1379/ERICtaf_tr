define([
    'jscore/core',
    'text!./TR.html',
    'styles!./TR.less',
    'styles!./cssPatches/_assetsPatch.less'
], function (core, template, styles, assetsPatch) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.navigation = element.find('.eaTR-Navigation');
            this.contentBlock = element.find('.eaTR-Content');
            this.version = element.find('.eaTR-Version');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return assetsPatch + styles;
        },

        getNavigation: function () {
            return this.navigation;
        },

        getContentBlock: function () {
            return this.contentBlock;
        },

        getVersion: function () {
            return this.version;
        }

    });

});
