/*global define*/
define([
    'jscore/core',
    'widgets/utils/domUtils',
    './CriterionListWidgetView',
    './criterionWidget/CriterionWidget'
], function (core, domUtils, View, CriterionWidget) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this._widgets = [];
            this.fields = options.fields;
        },

        onViewReady: function () {
            this.view.afterRender();
            this.view.getAddBlock().addEventHandler('click', this.onAddBlockClick, this);
        },

        redraw: function (criterions) {
            this._widgets.forEach(function (widgetObj) {
                widgetObj.destroy();
            });
            this._widgets = [];

            criterions.forEach(function (criterionObj, index) {
                var criterion = new CriterionWidget({
                    index: index,
                    data: criterionObj,
                    fields: this.fields
                });

                criterion.attachTo(this.view.getListHolder());
                this._widgets.push(criterion);

                criterion.addEventHandler(CriterionWidget.CLEAR_EVENT, this.onCriterionClearEvent, this);
            }.bind(this));

            this.resizeBlock();
        },

        resizeBlock: function () {
            var listHolderDimensions = domUtils.getElementDimensions(this.view.getListHolder()),
                addBlockDimensions = domUtils.getElementDimensions(this.view.getAddBlock());

            this.view.getRoot().setStyle('height', listHolderDimensions.height + addBlockDimensions.height + 8);
        },

        onAddBlockClick: function (event) {
            event.preventDefault();
            var length = this._widgets.length;

            var criterion = new CriterionWidget({
                index: length,
                fields: this.fields
            });
            criterion.attachTo(this.view.getListHolder());
            this._widgets.push(criterion);

            criterion.addEventHandler(CriterionWidget.CLEAR_EVENT, this.onCriterionClearEvent, this);

            this.resizeBlock();
        },

        onCriterionClearEvent: function (index) {
            this._widgets[index].destroy();
            this._widgets.splice(index, 1);
            updateWidgetsIndexes.call(this);

            this.resizeBlock();
        },

        isValidCriterions: function () {
            var isValid = true;
            this._widgets.forEach(function (widgetObj) {
                if (!widgetObj.isValid()) {
                    isValid = false;
                }
                widgetObj.markInvalid();
            });
            return isValid;
        },

        getCriterionsUrl: function () {
            var criterionsUrl = [];
            this._widgets.forEach(function (widgetObj) {
                criterionsUrl.push(widgetObj.getCriterionUrl());
            });
            return criterionsUrl.join('&');
        },

        clearCriteria: function () {
            this._widgets.forEach(function (widget) {
                widget.destroy();
            }, this);
            this.resizeBlock();
            this._widgets = [];
        }

    });

    /*************** PRIVATE FUNCTIONS ******************/

    function updateWidgetsIndexes () {
        this._widgets.forEach(function (widgetObj, index) {
            widgetObj.setIndex(index);
        });
    }

});
