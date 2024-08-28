define([
    '../../../../common/region/PageRegion',
    '../../../../common/Constants',
    './OperatorsMainRegionView',
    './panes/list/OperatorsPaneRegion/OperatorsPaneRegion',
    './panes/details/OperatorDetailsPaneRegion/OperatorDetailsPaneRegion',
    './panes/publicmethods/PublicMethodDetailsPaneRegion/PublicMethodDetailsPaneRegion',
    './models/OperatorModel'
], function (PageRegion, Constants, View, OperatorsPaneRegion, OperatorDetailsPaneRegion, PublicMethodDetailsPaneRegion,
             OperatorModel) {
    'use strict';

    return PageRegion.extend({
        /*jshint validthis:true*/

        View: View,

        onStart: function () {
            this.view.afterRender();
            this.operatorModel = new OperatorModel();

            this.operatorsPane = new OperatorsPaneRegion({context: this.getContext()});
            this.operatorDetailsPane = new OperatorDetailsPaneRegion({
                model: this.operatorModel,
                context: this.getContext()
            });
            this.publicMethodDetailsPane = new PublicMethodDetailsPaneRegion({
                model: this.operatorModel,
                context: this.getContext()
            });

            this.operatorsPane.start(this.view.getPanesHolder());
            this.operatorDetailsPane.start(this.view.getPanesHolder());
            this.publicMethodDetailsPane.start(this.view.getPanesHolder());
        },

        onShow: function (attributesObj, queryObj) {
            displayPanes.call(this, attributesObj, queryObj);
        },

        onHide: function () {
        },

        onRedraw: function (attributesObj, queryObj) {
            displayPanes.call(this, attributesObj, queryObj);
        }

    });

    function displayPanes (attributesObj, queryObj) {
        var viewScreensObj = {},
            isExpanded = queryObj && queryObj.expanded;
        viewScreensObj[Constants.pages.OPERATORS_PANE] = {left: '0%', width: '100%'};
        viewScreensObj[Constants.pages.OPERATOR_DETAILS_PANE] = {left: '100%', width: '0%'};
        viewScreensObj[Constants.pages.PUBLIC_METHOD_DETAILS_PANE] = {left: '100%', width: '0%'};

        showOrRedraw(this.operatorsPane, attributesObj, queryObj);
        this.operatorsPane.disableOverlay();

        if (attributesObj && attributesObj[Constants.urls.OPERATOR_ID_PARAM]) {
            this.operatorDetailsPane.disableOverlay();

            viewScreensObj[Constants.pages.OPERATORS_PANE] = {left: '0%', width: '50%'};
            if (isExpanded) {
                viewScreensObj[Constants.pages.OPERATOR_DETAILS_PANE] = {left: '5%', width: '95%'};
                this.operatorsPane.enableOverlay();
            } else {
                viewScreensObj[Constants.pages.OPERATOR_DETAILS_PANE] = {left: '50%', width: '50%'};
                this.operatorsPane.disableOverlay();
            }

            showOrRedraw(this.operatorDetailsPane, attributesObj, queryObj);
        } else {
            this.operatorDetailsPane.hide();
        }
        if (attributesObj && attributesObj[Constants.urls.PUBLIC_METHOD_ID_PARAM]) {
            this.operatorsPane.enableOverlay();

            viewScreensObj[Constants.pages.OPERATOR_DETAILS_PANE] = {left: '5%', width: '45%'};
            if (isExpanded) {
                viewScreensObj[Constants.pages.PUBLIC_METHOD_DETAILS_PANE] = {left: '12%', width: '88%'};
                this.operatorDetailsPane.enableOverlay();
            } else {
                viewScreensObj[Constants.pages.PUBLIC_METHOD_DETAILS_PANE] = {left: '50%', width: '50%'};
                this.operatorDetailsPane.disableOverlay();
            }

            showOrRedraw(this.publicMethodDetailsPane, attributesObj, queryObj);
            this.operatorDetailsPane.hideIcons();
        } else {
            this.publicMethodDetailsPane.hide();
            this.operatorDetailsPane.showIcons();
        }
        applyViewScreens.call(this, viewScreensObj);
    }

    function showOrRedraw (paneRegion, attributesObj, queryObj) {
        if (paneRegion.getShown()) {
            paneRegion.redraw(attributesObj, queryObj);
        } else {
            paneRegion.show(attributesObj, queryObj);
        }
    }

    function applyViewScreens (viewScreensObj) {
        applyViewScreen(this.operatorsPane, viewScreensObj[Constants.pages.OPERATORS_PANE]);
        applyViewScreen(this.operatorDetailsPane, viewScreensObj[Constants.pages.OPERATOR_DETAILS_PANE]);
        applyViewScreen(this.publicMethodDetailsPane, viewScreensObj[Constants.pages.PUBLIC_METHOD_DETAILS_PANE]);
    }

    function applyViewScreen (paneRegion, viewScreenObj) {
        paneRegion.applyViewScreen(viewScreenObj.left, viewScreenObj.width);
    }

});
