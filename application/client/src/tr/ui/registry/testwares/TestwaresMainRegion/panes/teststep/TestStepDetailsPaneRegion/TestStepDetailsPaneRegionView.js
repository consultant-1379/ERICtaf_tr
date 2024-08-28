define([
    'jscore/core',
    'text!./TestStepDetailsPaneRegion.html',
    'styles!./TestStepDetailsPaneRegion.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.overlay = element.find('.eaTR-TestwaresMainRegion-paneOverlay');
            this.iconsHolder = element.find('.ebLayout-HeadingCommands-iconHolder');
            this.name = element.find('.eaTR-TestStepDetailsPane-name');
            this.component = element.find('.eaTR-TestStepDetailsPane-component');
            this.description = element.find('.eaTR-TestStepDetailsPane-description');
            this.attributesTable = element.find('.eaTR-TestStepDetailsPane-attributesTable');
            this.returnType = element.find('.eaTR-TestStepDetailsPane-returnType');
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

        getComponent: function () {
            return this.component;
        },

        getDescription: function () {
            return this.description;
        },

        getAttributesTableHolder: function () {
            return this.attributesTable;
        },

        getReturnType: function () {
            return this.returnType;
        }

    });

});
