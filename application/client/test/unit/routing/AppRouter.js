/*global describe, it, expect, beforeEach, afterEach, sinon, setTimeout*/
define([
    'jscore/core',
    'tr/routing/AppRouter',
    'tr/common/Pages',
    'tr/common/Constants',
    'tr/lib/Crossroads'
], function (core, AppRouter, Pages, Constants, Crossroads) {

    'use strict';

    describe('AppRouter', function () {

        it('AppRouter should be defined', function () {
            expect(AppRouter).not.to.be.equal(undefined);
        });

        describe('AppRouter creation', function () {
            var appRouter, pages, server;

            beforeEach(function () {
                server = sinon.fakeServer.create();
                server.autoRespond = true;
                server.autoRespondAfter = 0;

                Constants.urls.APP_NAME = 'tr';

                var pagesObj = new Pages();
                pages = pagesObj.pages;

                appRouter = new AppRouter(pages, {}, new core.Element(), new core.Element());
            });

            afterEach(function () {
                server.restore();
            });

            it('should open default page with namespace', function (done) {
                Crossroads.parse(Constants.urls.APP_NAME);

                setTimeout(function () {
                    var loadedPage = appRouter.getLoadedPage(),
                        expectedPage = pages[Constants.pages.TESTWARE_LIST_PANE];

                    verifyPageData(loadedPage, expectedPage);

                    done();
                }, 100);
            });

            it('should open default page with namespace and testware', function (done) {
                Crossroads.parse(Constants.urls.APP_NAME + '/' + Constants.urls.DEFAULT_PAGE);

                setTimeout(function () {
                    var loadedPage = appRouter.getLoadedPage(),
                        expectedPage = pages[Constants.pages.TESTWARE_LIST_PANE];

                    verifyPageData(loadedPage, expectedPage);

                    done();
                }, 100);
            });

            it('should open testware details pane', function (done) {
                Crossroads.parse(Constants.urls.APP_NAME + '/' + Constants.urls.DEFAULT_PAGE + '/testwareId');

                setTimeout(function () {
                    var loadedPage = appRouter.getLoadedPage(),
                        expectedPage = pages[Constants.pages.TESTWARE_DETAILS_PANE];

                    verifyPageData(loadedPage, expectedPage);

                    done();
                }, 100);
            });

            it('should open test step details pane', function (done) {
                Crossroads.parse(Constants.urls.APP_NAME + '/' + Constants.urls.DEFAULT_PAGE + '/testwareId/testStepId');

                setTimeout(function () {
                    var loadedPage = appRouter.getLoadedPage(),
                        expectedPage = pages[Constants.pages.TEST_STEP_DETAILS_PANE];

                    verifyPageData(loadedPage, expectedPage);

                    done();
                }, 100);
            });

        });

    });

    function verifyPageData (loadedPage, expectedPage) {
        expect(expectedPage).not.to.be.equal(undefined);
        expect(loadedPage.isCreated).to.be.equal(true);
        expect(loadedPage.groupId).to.be.equal(expectedPage.groupId);
        expect(loadedPage.regionObj).not.to.be.equal(undefined);
        expect(loadedPage.breadcrumbObj).not.to.be.equal(undefined);
        expect(loadedPage.breadcrumbObj.breadcrumbItems.length).to.be.equal(expectedPage.breadcrumbData.length);
        expect(loadedPage.ContentRegion).to.eql(expectedPage.ContentRegion);
    }

});
