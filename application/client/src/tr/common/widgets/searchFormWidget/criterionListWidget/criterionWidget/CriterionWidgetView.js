/*global define*/
define([
    'jscore/core',
    'text!./_criterionWidget.html',
    'styles!./_criterionWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.field = element.find('.eaTR-Criterion-field');
            this.condition = element.find('.eaTR-Criterion-condition');
            this.valueHolder = element.find('.eaTR-Criterion-value');
            this.valueInput = element.find('.eaTR-Criterion-valueInput');
            this.actions = element.find('.eaTR-Criterion-actions');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getField: function () {
            return this.field;
        },

        getCondition: function () {
            return this.condition;
        },

        getValueHolder: function () {
            return this.valueHolder;
        },

        getValueInput: function () {
            return this.valueInput;
        },

        getActions: function () {
            return this.actions;
        }

    });

});
