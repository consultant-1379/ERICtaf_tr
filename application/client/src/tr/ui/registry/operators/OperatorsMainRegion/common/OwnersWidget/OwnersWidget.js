define([
    'jscore/core',
    './OwnersWidgetView'
], function (core, View) {
    'use strict';

    var Owners = core.Widget.extend({

        view: function () {
            var owners = this.options.owners;
            if (owners.length > 0) {
                owners[0].first = true;
            }
            return new View({
                template: {
                    owners: owners,
                    noOwnersMessage: Owners.NO_OWNERS_MESSAGE
                }
            });
        }

    }, {
        NO_OWNERS_MESSAGE: 'No owners are added here.'
    });

    return Owners;

});
