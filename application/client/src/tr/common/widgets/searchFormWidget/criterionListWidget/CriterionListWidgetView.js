/*global define*/
define([
    'jscore/core',
    'text!./_criterionListWidget.html',
    'styles!./_criterionListWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.listHolder = element.find('.eaTR-Criterions-list');
            this.addBlock = element.find('.eaTR-Criterions-addBlock');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getRoot: function () {
            return this.getElement();
        },

        getListHolder: function () {
            return this.listHolder;
        },

        getAddBlock: function () {
            return this.addBlock;
        }

    });

});
