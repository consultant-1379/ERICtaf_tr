/*global JSON*/
define([
    'jscore/core',
    './TRView',
    'text!./appInfo.json',
    './routing/AppRouter',
    './common/Constants',
    './common/Pages',
    './common/Navigation'
], function (core, View, appInfo, Router, Constants, Pages) {
    'use strict';

    return core.App.extend({

        View: View,

        init: function (options) {
            this.namespace = options.namespace;
            Constants.urls.APP_NAME = this.namespace;
        },

        onStart: function () {
            this.view.afterRender();

            this.eventBus = this.getContext().eventBus;

            var appInfoObj = JSON.parse(appInfo);
            this.view.getVersion().setText(appInfoObj.version);

            this.pagesObj = new Pages();
            this.router = new Router(this.pagesObj.pages, this.getContext(), this.view.getContentBlock(), this.view.getNavigation());
            this.router.start();
        },

        onStop: function () {
            this.router.stop();
        }

    });

});
