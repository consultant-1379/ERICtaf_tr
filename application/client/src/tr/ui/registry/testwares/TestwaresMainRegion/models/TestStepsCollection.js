/*global Error*/
define([
    '../../../../../ext/mvpCollection',
    './TestStepModel'
], function (mvpCollection, TestStepModel) {
    'use strict';

    return mvpCollection.extend({

        Model: TestStepModel,

        init: function () {
            this.testwareId = '';
        },

        url: function () {
            if (this.testwareId === '') {
                throw new Error('Testware Id should be defined for TestStepCollection!');
            }
            return '/registry/api/testware/' + this.testwareId + '/test-steps/';
        },

        setTestwareId: function (testwareId) {
            this.testwareId = testwareId;
        }

    });
});
