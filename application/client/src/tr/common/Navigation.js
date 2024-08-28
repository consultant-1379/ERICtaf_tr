/*global location*/
define([
    './Constants'
], function (Constants) {
    'use strict';

    var Navigation = {

        // Common functions
        navigateTo: function (url) {
            location.hash = url;
        },

        // Default url
        getDefaultUrl: function () {
            return Constants.urls.APP_NAME + '/' + Constants.urls.DEFAULT_PAGE;
        },

        getTestwareDetailsUrl: function () {
            return Navigation.getDefaultUrl() + '/:' + Constants.urls.TESTWARE_ID_PARAM;
        },

        getTestwareDetailsUrlWithParams: function (testwareId) {
            var url = Navigation.getTestwareDetailsUrl();
            return url.replace(':' + Constants.urls.TESTWARE_ID_PARAM, testwareId);
        },

        getTestStepDetailsUrl: function () {
            return Navigation.getTestwareDetailsUrl() + '/:' + Constants.urls.TEST_STEP_ID_PARAM;
        },

        getTestStepDetailsUrlWithParams: function (testwareId, testStepId) {
            return Navigation.getTestwareDetailsUrlWithParams(testwareId) + '/' + testStepId;
        },

        getOperatorsUrl: function () {
            return Constants.urls.APP_NAME + '/' + Constants.urls.OPERATORS_PAGE;
        },

        getOperatorDetailsUrl: function () {
            return Navigation.getOperatorsUrl() + '/:' + Constants.urls.OPERATOR_ID_PARAM;
        },

        getOperatorDetailsUrlWithParams: function (operatorId) {
            var url = Navigation.getOperatorDetailsUrl();
            return url.replace(':' + Constants.urls.OPERATOR_ID_PARAM, operatorId);
        },

        getPublicMethodDetailsUrl: function () {
            return Navigation.getOperatorDetailsUrl() + '/:' + Constants.urls.PUBLIC_METHOD_ID_PARAM;
        },

        getPublicMethodDetailsUrlWithParams: function (operatorId, publicMethodId) {
            return Navigation.getOperatorDetailsUrlWithParams(operatorId) + '/' + publicMethodId;
        }

    };

    return Navigation;

});
