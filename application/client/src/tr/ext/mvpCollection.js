/*global define, encodeURIComponent*/
define([
    'jscore/ext/mvp'
], function (mvp) {
    'use strict';

    return mvp.Collection.extend({

        reset: function () {
            var collection = this._collection;
            return collection.reset.apply(collection, arguments);
        },

        escape: function (string) {
            return encodeURIComponent(string);
        }

    });

});
