define([
    '../../../../../ext/mvpModel'
], function (mvpModel) {
    'use strict';

    return mvpModel.extend({

        url: '/registry/api/operators/',

        getId: function () {
            return this.getAttribute('id');
        },

        setId: function (id) {
            this.setAttribute('id', id);
        },

        getName: function () {
            return this.getAttribute('name');
        },

        getGav: function () {
            return this.getAttribute('gav');
        },

        getContext: function () {
            return this.getAttribute('context');
        },

        getPublicMethods: function () {
            return this.getAttribute('publicMethods');
        },

        getPublicMethodsCount: function () {
            return this.getAttribute('publicMethodsCount');
        }

    });
});
