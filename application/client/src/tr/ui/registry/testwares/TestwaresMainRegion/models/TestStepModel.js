/*global Error*/
define([
    '../../../../../ext/mvpModel'
], function (mvpModel) {
    'use strict';

    return mvpModel.extend({

        init: function () {
            this.testwareId = '';
        },

        url: function () {
            if (this.testwareId === '') {
                throw new Error('Testware Id should be defined for TestStepModel!');
            }
            return '/registry/api/testware/' + this.testwareId + '/test-steps/';
        },

        setTestwareId: function (testwareId) {
            this.testwareId = testwareId;
        },

        getId: function () {
            return this.getAttribute('id');
        },

        setId: function (id) {
            this.setAttribute('id', id);
        },

        getName: function () {
            return this.getAttribute('name');
        },

        getDescription: function () {
            return this.getAttribute('description');
        },

        getContext: function () {
            return this.getAttribute('context');
        },

        getComponent: function () {
            return this.getAttribute('component');
        },

        getReturnType: function () {
            return this.getAttribute('returnType');
        },

        getAttributes: function () {
            return this.getAttribute('attributes');
        }

    });
});
