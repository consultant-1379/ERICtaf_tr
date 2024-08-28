define([
    '../../../../../ext/mvpCollection',
    './OperatorModel'
], function (mvpCollection, OperatorModel) {
    'use strict';

    return mvpCollection.extend({

        Model: OperatorModel,

        urlRoot: '/registry/api/operators/search/',

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
