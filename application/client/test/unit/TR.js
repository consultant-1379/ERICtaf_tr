/*global describe, it, expect, beforeEach*/
define([
    'jscore/core',
    'tr/TR',
    'tr/common/Constants'
], function (core, TR, Constants) {
    'use strict';

    describe('TR', function () {

        it('TR should be defined', function () {
            expect(TR).not.to.be.equal(undefined);
        });

        describe('App creation', function () {
            var trApp,
                namespace = 'trr';

            beforeEach(function () {
                trApp = new TR({
                    namespace: namespace
                });
            });

            it('should populate namespace constant to Constants', function () {
                expect(Constants.urls.APP_NAME).to.be.equal(namespace);
            });

            it('should not create eventBus, pagesObj and router', function () {
                expect(trApp.eventBus).to.be.equal(undefined);
                expect(trApp.pagesObj).to.be.equal(undefined);
                expect(trApp.router).to.be.equal(undefined);
                expect(trApp.view).to.be.equal(undefined);
            });

            it('should create eventBus, pagesObj and router', function () {
                trApp.start(new core.Element());
                expect(trApp.eventBus).not.to.be.equal(undefined);
                expect(trApp.pagesObj).not.to.be.equal(undefined);
                expect(trApp.router).not.to.be.equal(undefined);
                expect(trApp.view.getVersion().getText()).to.be.equal('$VERSION$');
            });

        });
    });

});
