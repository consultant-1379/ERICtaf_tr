define([
    'jscore/core',
    'text!./VersionItem.html',
    'styles!./VersionItem.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        }

    });

});
