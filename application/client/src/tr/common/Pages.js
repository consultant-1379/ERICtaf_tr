define([
    './Constants',
    './Navigation',
    '../ui/registry/testwares/TestwaresMainRegion/TestwaresMainRegion',
    '../ui/registry/operators/OperatorsMainRegion/OperatorsMainRegion'
], function (Constants, Navigation, TestwaresMainRegion, OperatorsMainRegion) {
    'use strict';

    return function () {
        this.pages = {};
        this.breadcrumbs = {};

        // Testware breadcrumbs
        this.breadcrumbs[Constants.pages.TESTWARE_LIST_PANE] = [
            {name: 'Test Registry', url: '#' + Navigation.getDefaultUrl(), children: [
                {name: 'Testware List', url: '#' + Navigation.getDefaultUrl()},
                {name: 'Operators List', url: '#' + Navigation.getOperatorsUrl()}
            ]},
            {name: 'Testware List', url: '#' + Navigation.getDefaultUrl()}
        ];

        this.breadcrumbs[Constants.pages.TESTWARE_DETAILS_PANE] = this.breadcrumbs[Constants.pages.TESTWARE_LIST_PANE].slice(0);
        this.breadcrumbs[Constants.pages.TESTWARE_DETAILS_PANE].push({name: 'Testware Details', url: '#' + Navigation.getTestwareDetailsUrl()});

        this.breadcrumbs[Constants.pages.TEST_STEP_DETAILS_PANE] = this.breadcrumbs[Constants.pages.TESTWARE_DETAILS_PANE].slice(0);
        this.breadcrumbs[Constants.pages.TEST_STEP_DETAILS_PANE].push({name: 'Test Step Details', url: '#' + Navigation.getTestStepDetailsUrl()});

        // Operators breadcrumbs
        this.breadcrumbs[Constants.pages.OPERATORS_PANE] = [
            {name: 'Test Registry', url: '#' + Navigation.getDefaultUrl(), children: [
                {name: 'Testware List', url: '#' + Navigation.getDefaultUrl()},
                {name: 'Operators List', url: '#' + Navigation.getOperatorsUrl()}
            ]},
            {name: 'Operators List', url: '#' + Navigation.getOperatorsUrl()}
        ];

        this.breadcrumbs[Constants.pages.OPERATOR_DETAILS_PANE] = this.breadcrumbs[Constants.pages.OPERATORS_PANE].slice(0);
        this.breadcrumbs[Constants.pages.OPERATOR_DETAILS_PANE].push({name: 'Operator Details', url: '#' + Navigation.getOperatorDetailsUrl()});

        this.breadcrumbs[Constants.pages.PUBLIC_METHOD_DETAILS_PANE] = this.breadcrumbs[Constants.pages.OPERATOR_DETAILS_PANE].slice(0);
        this.breadcrumbs[Constants.pages.PUBLIC_METHOD_DETAILS_PANE].push({name: 'Public Method Details', url: '#' + Navigation.getPublicMethodDetailsUrl()});

        // Testware page (panes)
        this.pages[Constants.pages.TESTWARE_LIST_PANE] = {
            groupId: Constants.pages.TESTWARE_LIST_PAGE,
            isCreated: false,
            breadcrumbData: this.breadcrumbs[Constants.pages.TESTWARE_LIST_PANE],
            ContentRegion: TestwaresMainRegion
        };

        this.pages[Constants.pages.TESTWARE_DETAILS_PANE] = {
            groupId: Constants.pages.TESTWARE_LIST_PAGE,
            isCreated: false,
            breadcrumbData: this.breadcrumbs[Constants.pages.TESTWARE_DETAILS_PANE],
            ContentRegion: TestwaresMainRegion
        };

        this.pages[Constants.pages.TEST_STEP_DETAILS_PANE] = {
            groupId: Constants.pages.TESTWARE_LIST_PAGE,
            isCreated: false,
            breadcrumbData: this.breadcrumbs[Constants.pages.TEST_STEP_DETAILS_PANE],
            ContentRegion: TestwaresMainRegion
        };

        // Operators page (panes)
        this.pages[Constants.pages.OPERATORS_PANE] = {
            groupId: Constants.pages.OPERATORS_PAGE,
            isCreated: false,
            breadcrumbData: this.breadcrumbs[Constants.pages.OPERATORS_PANE],
            ContentRegion: OperatorsMainRegion
        };

        this.pages[Constants.pages.OPERATOR_DETAILS_PANE] = {
            groupId: Constants.pages.OPERATORS_PAGE,
            isCreated: false,
            breadcrumbData: this.breadcrumbs[Constants.pages.OPERATOR_DETAILS_PANE],
            ContentRegion: OperatorsMainRegion
        };

        this.pages[Constants.pages.PUBLIC_METHOD_DETAILS_PANE] = {
            groupId: Constants.pages.OPERATORS_PAGE,
            isCreated: false,
            breadcrumbData: this.breadcrumbs[Constants.pages.PUBLIC_METHOD_DETAILS_PANE],
            ContentRegion: OperatorsMainRegion
        };
    };

});
