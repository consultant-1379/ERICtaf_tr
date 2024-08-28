define([
    '../../../../../ext/mvpCollection',
    './TestwareModel'
], function (mvpCollection, TestwareModel) {
    'use strict';

    return mvpCollection.extend({

        Model: TestwareModel,

        urlRoot: '/registry/api/testware/search/',

        queryString: '',

        isQuickSearch: false,

        url: function () {
            return this.urlRoot + this.searchType() + this.queryString;
        },

        setQueryParameters: function (queryString, isQuickSearch) {
            this.queryString = queryString;
            this.isQuickSearch = isQuickSearch;
        },

        searchType: function () {
            return this.isQuickSearch ? 'any?' : 'query?';
        }

    });
});
