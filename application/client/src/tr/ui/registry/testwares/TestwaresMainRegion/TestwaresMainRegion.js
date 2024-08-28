define([
    '../../../../common/region/PageRegion',
    '../../../../common/Constants',
    './TestwaresMainRegionView',
    './panes/list/TestwaresPaneRegion/TestwaresPaneRegion',
    './panes/details/TestwareDetailsPaneRegion/TestwareDetailsPaneRegion',
    './panes/teststep/TestStepDetailsPaneRegion/TestStepDetailsPaneRegion',
    './models/TestwareModel'
], function (PageRegion, Constants, View, TestwaresPaneRegion, TestwareDetailsPaneRegion, TestStepDetailsPaneRegion, TestwareModel) {
    'use strict';

    return PageRegion.extend({
        /*jshint validthis:true*/

        View: View,

        onStart: function () {
            this.view.afterRender();
            this.testwareModel = new TestwareModel();

            this.testwaresPane = new TestwaresPaneRegion({context: this.getContext()});
            this.testwareDetailsPane = new TestwareDetailsPaneRegion({
                model: this.testwareModel,
                context: this.getContext()
            });
            this.testStepDetailsPane = new TestStepDetailsPaneRegion({
                model: this.testwareModel,
                context: this.getContext()
            });

            this.testwaresPane.start(this.view.getPanesHolder());
            this.testwareDetailsPane.start(this.view.getPanesHolder());
            this.testStepDetailsPane.start(this.view.getPanesHolder());
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
        viewScreensObj[Constants.pages.TESTWARE_LIST_PANE] = {left: '0%', width: '100%'};
        viewScreensObj[Constants.pages.TESTWARE_DETAILS_PANE] = {left: '100%', width: '0%'};
        viewScreensObj[Constants.pages.TEST_STEP_DETAILS_PANE] = {left: '100%', width: '0%'};

        showOrRedraw(this.testwaresPane, attributesObj, queryObj);
        this.testwaresPane.disableOverlay();

        if (attributesObj && attributesObj[Constants.urls.TESTWARE_ID_PARAM]) {
            this.testwareDetailsPane.disableOverlay();

            viewScreensObj[Constants.pages.TESTWARE_LIST_PANE] = {left: '0%', width: '50%'};
            if (isExpanded) {
                viewScreensObj[Constants.pages.TESTWARE_DETAILS_PANE] = {left: '5%', width: '95%'};
                this.testwaresPane.enableOverlay();
            } else {
                viewScreensObj[Constants.pages.TESTWARE_DETAILS_PANE] = {left: '50%', width: '50%'};
                this.testwaresPane.disableOverlay();
            }

            showOrRedraw(this.testwareDetailsPane, attributesObj, queryObj);
        } else {
            this.testwareDetailsPane.hide();
        }
        if (attributesObj && attributesObj[Constants.urls.TEST_STEP_ID_PARAM]) {
            this.testwaresPane.enableOverlay();

            viewScreensObj[Constants.pages.TESTWARE_DETAILS_PANE] = {left: '5%', width: '45%'};
            if (isExpanded) {
                viewScreensObj[Constants.pages.TEST_STEP_DETAILS_PANE] = {left: '12%', width: '88%'};
                this.testwareDetailsPane.enableOverlay();
            } else {
                viewScreensObj[Constants.pages.TEST_STEP_DETAILS_PANE] = {left: '50%', width: '50%'};
                this.testwareDetailsPane.disableOverlay();
            }

            showOrRedraw(this.testStepDetailsPane, attributesObj, queryObj);
            this.testwareDetailsPane.hideIcons();
        } else {
            this.testStepDetailsPane.hide();
            this.testwareDetailsPane.showIcons();
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
        applyViewScreen(this.testwaresPane, viewScreensObj[Constants.pages.TESTWARE_LIST_PANE]);
        applyViewScreen(this.testwareDetailsPane, viewScreensObj[Constants.pages.TESTWARE_DETAILS_PANE]);
        applyViewScreen(this.testStepDetailsPane, viewScreensObj[Constants.pages.TEST_STEP_DETAILS_PANE]);
    }

    function applyViewScreen (paneRegion, viewScreenObj) {
        paneRegion.applyViewScreen(viewScreenObj.left, viewScreenObj.width);
    }

});
