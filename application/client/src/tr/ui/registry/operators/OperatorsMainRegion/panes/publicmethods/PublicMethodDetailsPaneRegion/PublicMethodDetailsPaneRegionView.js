define([
    'jscore/core',
    'text!./PublicMethodDetailsPaneRegion.html',
    'styles!./PublicMethodDetailsPaneRegion.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.overlay = element.find('.eaTR-OperatorsMainRegion-paneOverlay');
            this.iconsHolder = element.find('.ebLayout-HeadingCommands-iconHolder');
            this.name = element.find('.eaTR-PublicMethodDetailsPane-name');
            this.attributesTable = element.find('.eaTR-PublicMethodDetailsPane-attributesTable');
            this.returnType = element.find('.eaTR-PublicMethodDetailsPane-returnType');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        },

        getOverlay: function () {
            return this.overlay;
        },

        getIconsHolder: function () {
            return this.iconsHolder;
        },

        getName: function () {
            return this.name;
        },

        getAttributesTableHolder: function () {
            return this.attributesTable;
        },

        getReturnType: function () {
            return this.returnType;
        }

    });

});
