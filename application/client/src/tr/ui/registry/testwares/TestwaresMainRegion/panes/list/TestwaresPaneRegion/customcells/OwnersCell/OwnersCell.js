define([
    'tablelib/Cell',
    '../../../../../common/OwnersWidget/OwnersWidget'
], function (Cell, OwnersWidget) {
    'use strict';

    return Cell.extend({
        /*jshint validthis:true */

        setValue: function (data) {
            var ownersWidget = new OwnersWidget({
                owners: data
            });
            this.ownersIds = getOwnersIds(data);
            ownersWidget.attachTo(this.getElement());
        },

        setTooltip: function () {
            this.view.getElement().setAttribute('title', getItemsStr.call(this));
        }

    });

    function getItemsStr () {
        if (this.ownersIds && this.ownersIds.length > 0) {
            return this.ownersIds.join(', ');
        }
        return OwnersWidget.NO_OWNERS_MESSAGE;
    }

    function getOwnersIds (data) {
        var ids = [];
        data.forEach(function (ownerObj) {
            ids.push(ownerObj.id);
        });
        return ids;
    }

});
