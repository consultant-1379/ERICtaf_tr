define([
    'jscore/ext/utils/base/underscore',
    'jscore/ext/binding',
    '../../../../../../../common/region/PaneRegion',
    '../../../../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../../../../common/Navigation',
    '../../../../../../../common/Constants',
    'tablelib/Table',
    'tablelib/plugins/SortableHeader',
    './PublicMethodDetailsPaneRegionView'
], function (_, binding, PaneRegion, ActionIcon, Navigation, Constants, Table, SortableHeader, View) {
    'use strict';

    return PaneRegion.extend({
        /*jshint validthis:true*/

        View: View,

        init: function () {
            this.isExpanded = false;
            this.operatorId = null;
            this.publicMethodId = null;
            this.operatorModel = this.options.model;
            this.currentPublicMethod = null;
        },

        onStart: function () {
            this.view.afterRender();

            this.attributesTable = new Table({
                plugins: [
                    new SortableHeader()
                ],
                modifiers: [
                    {name: 'striped'}
                ],
                columns: [
                    {title: 'Name', attribute: 'name', width: '25%', sortable: true},
                    {title: 'Type', attribute: 'type', width: '15%', sortable: true}
                ]
            });
            this.attributesTable.attachTo(this.view.getAttributesTableHolder());

            this.attributesTable.addEventHandler('sort', function (sortMode, attribute) {
                this.attributesTable.setData(getSortedData(this.currentPublicMethod.attributes, sortMode, attribute));
            }.bind(this));

            this.getEventBus().subscribe(Constants.events.SHOW_PUBLIC_METHOD_DETAILS, function (publicMethodId) {
                this.onShowPublicMethodDetails(publicMethodId);
            }, this);

            initExpandIcon.call(this);
            initCloseIcon.call(this);
        },

        onShowPublicMethodDetails: function (publicMethodId) {
            this.currentPublicMethod = _.findWhere(this.operatorModel.toJSON().publicMethods, {id: publicMethodId});
            if (!this.currentPublicMethod) {
                return;
            }

            this.view.getName().setText(this.currentPublicMethod.name);
            this.view.getReturnType().setText(this.currentPublicMethod.returnType);

            this.attributesTable.setData(this.currentPublicMethod.attributes);
        },

        onCloseIconClick: function () {
            Navigation.navigateTo(Navigation.getOperatorDetailsUrlWithParams(this.operatorId));
        },

        onExpandCollapseIconClick: function () {
            var url = Navigation.getPublicMethodDetailsUrlWithParams(this.operatorId, this.publicMethodId);
            if (!this.isExpanded) {
                url += '?expanded=true';
            }
            Navigation.navigateTo(url);
        },

        onShow: function (attributesObj, queryObj) {
            updatePageAttributes.call(this, attributesObj);
            this.isExpanded = (queryObj && queryObj.expanded === 'true');
            updateExpandCollapseIcon.call(this);
        },

        onRedraw: function (attributesObj, queryObj) {
            updatePageAttributes.call(this, attributesObj);
            this.isExpanded = (queryObj && queryObj.expanded === 'true');
            updateExpandCollapseIcon.call(this);
        }

    });

    function getSortedData (data, sortMode, sortAttr) {
        var sortNum = sortMode === 'asc' ? -1 : 1;
        data.sort(function (a, b) {
            if (a[sortAttr] < b[sortAttr]) {
                return sortNum;
            }
            if (a[sortAttr] > b[sortAttr]) {
                return -1 * sortNum;
            }
            return 0;
        });
        return data;
    }

    function updatePageAttributes (attributesObj) {
        this.operatorId = attributesObj[Constants.urls.OPERATOR_ID_PARAM];
        this.publicMethodId = attributesObj[Constants.urls.PUBLIC_METHOD_ID_PARAM];
    }

    function updateExpandCollapseIcon () {
        if (this.isExpanded) {
            this.expandCollapseIcon.setIcon('rightArrowLarge');
            this.expandCollapseIcon.setTitle('Collapse');
        } else {
            this.expandCollapseIcon.setIcon('leftArrowLarge');
            this.expandCollapseIcon.setTitle('Expand');
        }
    }

    function initCloseIcon () {
        this.closeIcon = new ActionIcon({
            title: 'Close',
            iconKey: 'close',
            iconSize: 'headerSize',
            interactive: true
        });
        this.closeIcon.attachTo(this.view.getIconsHolder());
        this.closeIcon.addEventHandler('click', this.onCloseIconClick, this);
    }

    function initExpandIcon () {
        this.expandCollapseIcon = new ActionIcon({
            title: 'Expand',
            iconKey: 'leftArrowLarge',
            iconSize: 'headerSize',
            interactive: true
        });
        this.expandCollapseIcon.attachTo(this.view.getIconsHolder());
        this.expandCollapseIcon.addEventHandler('click', this.onExpandCollapseIconClick, this);
    }

});
