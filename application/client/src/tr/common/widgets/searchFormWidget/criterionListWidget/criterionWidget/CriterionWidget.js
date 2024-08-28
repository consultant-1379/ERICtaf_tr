define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'widgets/SelectBox',
    './CriterionWidgetView',
    '../../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../../ext/stringUtils'
], function (core, _, SelectBox, View, ActionIcon, stringUtils) {
    'use strict';

    var Criterion = core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        DEFAULT_FIELD_OBJECT: {name: 'Field name', value: ''},
        DEFAULT_CONDITION_OBJECT: {name: 'Condition', value: ''},

        init: function (options) {
            this.criterion = {
                field: '',
                condition: '',
                value: ''
            };
            this.itemIndex = options.index;
            this.fields = options.fields;
            this.conditions = getConditions();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.fieldSelectBox = new SelectBox({
                items: this.fields,
                value: this.DEFAULT_FIELD_OBJECT,
                modifiers: [
                    {name: 'width', value: 'small'}
                ]
            });
            this.fieldSelectBox.attachTo(this.view.getField());
            this.fieldSelectBox.view.getRoot().setAttribute('id',
                'TR_TestwareSearch_Criterion-fieldSelectBox-' + this.itemIndex);

            this.conditionSelectBox = new SelectBox({
                items: this.conditions,
                value: this.DEFAULT_CONDITION_OBJECT,
                modifiers: [
                    {name: 'width', value: 'small'}
                ]
            });
            this.conditionSelectBox.attachTo(this.view.getCondition());
            this.conditionSelectBox.view.getRoot().setAttribute('id',
                'TR_TestwareSearch_Criterion-conditionSelectBox-' + this.itemIndex);

            this.actionIcon = new ActionIcon({
                iconKey: 'close',
                interactive: true
            });
            this.actionIcon.attachTo(this.view.getActions());

            if (this.options.data !== undefined) {
                var data = this.options.data,
                    fieldValue = _.findWhere(this.fields, {value: data.field}),
                    conditionValue = _.findWhere(this.conditions, {value: data.operator}),
                    value = data.value;

                this.fieldSelectBox.setValue(fieldValue);
                this.conditionSelectBox.setValue(conditionValue);
                this.view.getValueInput().setValue(value);

                this.criterion.field = data.field;
                this.criterion.condition = data.operator;
                this.criterion.value = data.value;
            }

            this.actionIcon.addEventHandler('click', this.onClearCondition, this);
            this.fieldSelectBox.addEventHandler('change', this.onFieldSelectBoxChange, this);
            this.conditionSelectBox.addEventHandler('change', this.onConditionSelectBoxChange, this);
            this.view.getValueInput().addEventHandler('input', this.onValueInputChange, this);
        },

        setIndex: function (index) {
            this.itemIndex = index;
        },

        onClearCondition: function () {
            this.trigger(Criterion.CLEAR_EVENT, this.itemIndex);
        },

        onFieldSelectBoxChange: function () {
            this.criterion.field = this.fieldSelectBox.getValue().value;
        },

        onConditionSelectBoxChange: function () {
            this.criterion.condition = this.conditionSelectBox.getValue().value;
        },

        onValueInputChange: function () {
            this.criterion.value = stringUtils.trim(this.view.getValueInput().getValue());
        },

        getCriterionUrl: function () {
            return this.criterion.field + this.criterion.condition + this.criterion.value;
        },

        markInvalid: function () {
            this.view.getField().setModifier('invalid', this.criterion.field === '' ? 'true' : 'false');
            this.view.getCondition().setModifier('invalid', this.criterion.condition === '' ? 'true' : 'false');
            this.view.getValueHolder().setModifier('invalid', this.criterion.value === '' ? 'true' : 'false');
        },

        isValid: function () {
            return this.criterion.field !== '' && this.criterion.condition !== '' && this.criterion.value !== '';
        }

    }, {
        CLEAR_EVENT: 'clearItem'
    });

    return Criterion;

    function getConditions () {
        return [
            {name: 'equals', title: 'equals', value: '='},
            {name: 'not equal', title: 'not equal', value: '!='},
            {name: 'contains', title: 'contains', value: '~'}
        ];
    }

});
