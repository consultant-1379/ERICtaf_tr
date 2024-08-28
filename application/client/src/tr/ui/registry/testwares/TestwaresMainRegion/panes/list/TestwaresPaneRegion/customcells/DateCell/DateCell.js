/*global Date*/
define([
    'tablelib/Cell'
], function (Cell) {
    'use strict';

    return Cell.extend({

        setValue: function(value) {
            this.getElement().setText(formatAsDate(value));
        }

    });

    function formatAsDate(value) {
        var date = new Date(value);
        return date.toLocaleString();
    }

});
