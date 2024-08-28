define(function () {
    'use strict';

    var Constants = {};

    Constants.urls = {
        APP_NAME: '',
        DEFAULT_PAGE: 'testware',
        OPERATORS_PAGE: 'operators',
        TESTWARE_ID_PARAM: 'testwareId',
        TEST_STEP_ID_PARAM: 'testStepId',
        OPERATOR_ID_PARAM: 'operatorId',
        PUBLIC_METHOD_ID_PARAM: 'publicMethodId'
    };

    Constants.pages = {
        TESTWARE_LIST_PAGE: 'testwareListPage',
        TESTWARE_LIST_PANE: 'testwareListPane',
        TESTWARE_DETAILS_PANE: 'testwareDetailsPane',
        TEST_STEP_DETAILS_PANE: 'testStepDetailsPane',

        OPERATORS_PAGE: 'operatorsPage',
        OPERATORS_PANE: 'operatorsPane',
        OPERATOR_DETAILS_PANE: 'operatorDetailsPane',
        PUBLIC_METHOD_DETAILS_PANE: 'publicMethodDetailsPane'
    };

    Constants.events = {
        SHOW_TEST_STEP_DETAILS: 'showTestStepDetails',
        SHOW_PUBLIC_METHOD_DETAILS: 'showPublicMethodDetails'
    };

    return Constants;

});
