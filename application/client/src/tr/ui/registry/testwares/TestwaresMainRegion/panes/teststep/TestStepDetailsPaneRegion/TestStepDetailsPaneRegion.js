define([
    'jscore/ext/utils/base/underscore',
    'jscore/ext/binding',
    '../../../../../../../common/region/PaneRegion',
    '../../../../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../../../../common/Navigation',
    '../../../../../../../common/Constants',
    'tablelib/Table',
    'tablelib/plugins/SortableHeader',
    './TestStepDetailsPaneRegionView'
], function (_, binding, PaneRegion, ActionIcon, Navigation, Constants, Table, SortableHeader, View) {
    'use strict';

    return PaneRegion.extend({
        /*jshint validthis:true*/

        View: View,

        init: function () {
            this.isExpanded = false;
            this.testwareId = null;
            this.testStepId = null;
            this.testwareModel = this.options.model;
            this.currentTestStep = null;
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
                this.attributesTable.setData(getSortedData(this.currentTestStep.attributes, sortMode, attribute));
            }.bind(this));

            this.getEventBus().subscribe(Constants.events.SHOW_TEST_STEP_DETAILS, function (testStepId) {
                this.onShowTestStepDetails(testStepId);
            }, this);

            initExpandIcon.call(this);
            initCloseIcon.call(this);
        },

        onShowTestStepDetails: function (testStepId) {
            this.currentTestStep = _.findWhere(this.testwareModel.toJSON().testSteps, {id: testStepId});
            if (!this.currentTestStep) {
                return;
            }
            
            this.view.getName().setText(this.currentTestStep.name);
            this.view.getComponent().setText(this.currentTestStep.component);
            this.view.getDescription().setText(this.currentTestStep.description);
            this.view.getReturnType().setText(this.currentTestStep.returnType);

            this.attributesTable.setData(this.currentTestStep.attributes);
        },

        onCloseIconClick: function () {
            Navigation.navigateTo(Navigation.getTestwareDetailsUrlWithParams(this.testwareId));
        },

        onExpandCollapseIconClick: function () {
            var url = Navigation.getTestStepDetailsUrlWithParams(this.testwareId, this.testStepId);
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
        this.testwareId = attributesObj[Constants.urls.TESTWARE_ID_PARAM];
        this.testStepId = attributesObj[Constants.urls.TEST_STEP_ID_PARAM];
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
