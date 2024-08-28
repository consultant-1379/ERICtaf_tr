define([
    '../../../../../ext/mvpModel'
], function (mvpModel) {
    'use strict';

    return mvpModel.extend({

        url: '/registry/api/testware/',

        setId: function (id) {
            this.setAttribute('id', id);
        },

        getId: function () {
            return this.getAttribute('id');
        },

        getJavaDocLocation: function () {
            return this.getAttribute('javaDocLocation');
        },

        getGitLocation: function () {
            return this.getAttribute('gitLocation');
        },

        getPomLocation: function () {
            return this.getAttribute('pomLocation');
        },

        getDescription: function () {
            return this.getAttribute('description');
        },

        getOwners: function () {
            return this.getAttribute('owners');
        },

        getSuites: function () {
            return this.getAttribute('suites');
        },

        getPublishedAt: function () {
            return this.getAttribute('publishedAt');
        },

        getGroupId: function () {
            return this.getAttribute('groupId');
        },

        getArtifactId: function () {
            return this.getAttribute('artifactId');
        },

        getVersion: function () {
            return this.getAttribute('version');
        },

        getTafVersion: function () {
            return this.getAttribute('tafVersion');
        },

        getTotalTestStepsCount: function () {
            return this.getAttribute('totalTestStepsCount');
        },

        getSharedTestStepsCount: function () {
            return this.getAttribute('sharedTestStepsCount');
        }


    });
});
