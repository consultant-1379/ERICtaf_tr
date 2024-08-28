/*global window, location*/
define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    '../lib/Crossroads',
    'widgets/Breadcrumb',
    '../common/Constants'
], function (core, _, Crossroads, Breadcrumb, Constants) {
    'use strict';

    function onHashChange () {
        /*jshint validthis:true*/
        var match = location.href.match(/#(.*)$/);
        Crossroads.parse(match ? match[1] : '');
    }

    function removeChildren (children) {
        children.forEach(function (childEl) {
            childEl.remove();
        });
    }

    var pageLoaded = null;

    function applyContentRegion (pageId, attributesObj, queryObj) {
        /*jshint validthis:true*/
        if (this.pagesObj[pageId]) {
            var pageToOpen = this.pagesObj[pageId];

            if (pageLoaded !== null) {
                if (pageLoaded.groupId === pageToOpen.groupId && !pageToOpen.isCreated) {
                    pageToOpen.regionObj.redraw(attributesObj, queryObj);
                } else {
                    pageLoaded.regionObj.hide();
                    if (pageToOpen.isCreated) {
                        pageToOpen.regionObj.show(attributesObj, queryObj);
                    }
                }
            }

            if (pageLoaded === null || !pageToOpen.isCreated) {
                pageToOpen.regionObj = new pageToOpen.ContentRegion({context: this.appContext});
                pageToOpen.regionObj.start(this.contentHolderEl);
                pageToOpen.regionObj.show(attributesObj, queryObj);
                pageToOpen.isCreated = true;
            }

            pageLoaded = pageToOpen;
        }
    }

    function applyBreadcrumbs (pageId, attributesObj) {
        /*jshint validthis:true*/
        if (this.pagesObj[pageId]) {
            var pageToOpen = this.pagesObj[pageId];

            removeChildren(this.breadcrumbHolderEl.children());
            if (pageLoaded.breadcrumbObj) {
                pageLoaded.breadcrumbObj.destroy();
            }

            // replace breadcrumbs with fresh data
            var breadcrumbDataCopy = pageToOpen.breadcrumbData.slice(0);
            pageLoaded.breadcrumbObj = new Breadcrumb({
                data: replaceWithIds(breadcrumbDataCopy, attributesObj)
            });
            pageLoaded.breadcrumbObj.attachTo(this.breadcrumbHolderEl);
        }
    }

    function replaceWithIds (breadcrumbData, attributesObj) {
        if (!attributesObj) {
            return breadcrumbData;
        }
        var breadcrumbs = [];
        breadcrumbData.forEach(function (breadcrumbObj) {
            var url = breadcrumbObj.url;
            _.keys(attributesObj).forEach(function (key) {
                url = url.replace(':' + key, attributesObj[key]);
            });
            var children = [];
            if (breadcrumbObj.hasOwnProperty('children') && breadcrumbObj.children.length > 0) {
                children = replaceWithIds(breadcrumbObj.children, attributesObj);
            }

            var breadcrumb = {
                name: breadcrumbObj.name,
                url: url
            };
            if (children.length > 0) {
                breadcrumb.children = children;
            }
            breadcrumbs.push(breadcrumb);
        });
        return breadcrumbs;
    }

    function showTestwareListPage () {
        /*jshint validthis:true*/
        applyContentRegion.call(this, Constants.pages.TESTWARE_LIST_PANE);
        applyBreadcrumbs.call(this, Constants.pages.TESTWARE_LIST_PANE);
    }

    function showTestwareDetails (testwareId, queryObj) {
        /*jshint validthis:true*/
        var attributesObj = {};
        attributesObj[Constants.urls.TESTWARE_ID_PARAM] = testwareId;

        applyContentRegion.call(this, Constants.pages.TESTWARE_LIST_PANE, attributesObj, queryObj);
        applyBreadcrumbs.call(this, Constants.pages.TESTWARE_DETAILS_PANE, attributesObj);
    }

    function showTestStepDetails (testwareId, testStepId, queryObj) {
        /*jshint validthis:true*/
        var attributesObj = {};
        attributesObj[Constants.urls.TESTWARE_ID_PARAM] = testwareId;
        attributesObj[Constants.urls.TEST_STEP_ID_PARAM] = testStepId;

        applyContentRegion.call(this, Constants.pages.TESTWARE_LIST_PANE, attributesObj, queryObj);
        applyBreadcrumbs.call(this, Constants.pages.TEST_STEP_DETAILS_PANE, attributesObj);
    }

    function showOperatorsPage() {
        /*jshint validthis:true*/
        applyContentRegion.call(this, Constants.pages.OPERATORS_PANE);
        applyBreadcrumbs.call(this, Constants.pages.OPERATORS_PANE);
    }

    function showOperatorDetails (operatorId, queryObj) {
        /*jshint validthis:true*/
        var attributesObj = {};
        attributesObj[Constants.urls.OPERATOR_ID_PARAM] = operatorId;

        applyContentRegion.call(this, Constants.pages.OPERATORS_PANE, attributesObj, queryObj);
        applyBreadcrumbs.call(this, Constants.pages.OPERATOR_DETAILS_PANE, attributesObj);
    }

    function showPublicMethodDetails (operatorId, publicMethodId, queryObj) {
        /*jshint validthis:true*/
        var attributesObj = {};
        attributesObj[Constants.urls.OPERATOR_ID_PARAM] = operatorId;
        attributesObj[Constants.urls.PUBLIC_METHOD_ID_PARAM] = publicMethodId;

        applyContentRegion.call(this, Constants.pages.OPERATORS_PANE, attributesObj, queryObj);
        applyBreadcrumbs.call(this, Constants.pages.PUBLIC_METHOD_DETAILS_PANE, attributesObj);
    }

    var AppRouter = function (pagesObj, appContext, contentHolderEl, breadcrumbHolderEl) {
        this.pagesObj = pagesObj;
        this.appContext = appContext;
        this.contentHolderEl = contentHolderEl;
        this.breadcrumbHolderEl = breadcrumbHolderEl;
    };

    AppRouter.prototype.getLoadedPage = function () {
        return pageLoaded;
    };

    AppRouter.prototype.start = function () {
        Crossroads.addRoute('tr', showTestwareListPage.bind(this));
        // Testware pages
        Crossroads.addRoute('tr/testware', showTestwareListPage.bind(this));
        Crossroads.addRoute('tr/testware/{' + Constants.urls.TESTWARE_ID_PARAM + '}:?query:', showTestwareDetails.bind(this));
        Crossroads.addRoute('tr/testware/{' + Constants.urls.TESTWARE_ID_PARAM + '}/{' + Constants.urls.TEST_STEP_ID_PARAM + '}:?query:', showTestStepDetails.bind(this));

        // Operators pages
        Crossroads.addRoute('tr/operators', showOperatorsPage.bind(this));
        Crossroads.addRoute('tr/operators/{' + Constants.urls.OPERATOR_ID_PARAM + '}:?query:', showOperatorDetails.bind(this));
        Crossroads.addRoute('tr/operators/{' + Constants.urls.OPERATOR_ID_PARAM + '}/{' + Constants.urls.PUBLIC_METHOD_ID_PARAM + '}:?query:', showPublicMethodDetails.bind(this));

        window.addEventListener('hashchange', onHashChange, false);
        onHashChange();
    };

    AppRouter.prototype.stop = function () {
        window.removeEventListener('hashchange');
    };

    return AppRouter;

});
