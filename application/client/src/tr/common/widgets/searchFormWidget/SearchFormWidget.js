/*global define*/
define([
    'jscore/core',
    'widgets/utils/domUtils',
    './SearchFormWidgetView',
    './criterionListWidget/CriterionListWidget'
], function (core, domUtils, View, CriterionListWidget) {
    'use strict';

    return core.Widget.extend({

        View: View,

        onViewReady: function () {
            this.view.afterRender();

            this.region = this.options.region;
            this.criterions = new CriterionListWidget({
                fields: this.options.fields
            });
            this.criterions.attachTo(this.view.getContentBlock());

            this.view.getSearchButton().addEventHandler('click', this.onSearchButtonClick, this);
        },

        updateAdvancedSearch: function (criterions) {
            this.criterions.redraw(criterions);
        },

        onSearchButtonClick: function (event) {
            event.preventDefault();
            if (this.criterions.isValidCriterions()) {
                var criterionsUrl = this.criterions.getCriterionsUrl();
                this.region.runSearch(criterionsUrl, false);
            }
        },

        resizeBlock: function () {
            var rootElDimensions = domUtils.getElementDimensions(this.getElement());

            if (!this.headerHeight) {
                this.headerHeight = domUtils.getElementDimensions(this.view.getHeaderBlock()).height;
            }
            if (!this.buttonHeight) {
                this.buttonHeight = domUtils.getElementDimensions(this.view.getButtonBlock()).height;
            }

            this.view.getContentBlock().setStyle('max-height', rootElDimensions.height - this.buttonHeight - this.headerHeight - 20);
            this.criterions.resizeBlock();
        },

        clearCriteria: function () {
            this.criterions.clearCriteria();
        }

    });

});
