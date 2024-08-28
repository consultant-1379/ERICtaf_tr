/*global define*/
define([
    'jscore/ext/mvp'
], function (mvp) {
    'use strict';

    return mvp.Model.extend({

        clear: function () {
            var model = this._model;
            return model.clear.apply(model, arguments);
        }

    });

});