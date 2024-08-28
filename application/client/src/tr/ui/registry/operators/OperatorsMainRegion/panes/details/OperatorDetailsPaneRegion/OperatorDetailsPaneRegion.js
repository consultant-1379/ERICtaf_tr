define([
    'jscore/ext/utils/base/underscore',
    'jscore/ext/binding',
    '../../../../../../../common/region/PaneRegion',
    '../../../../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../../../../common/widgets/actionLink/ActionLink',
    '../../../../../../../common/widgets/progressBlock/ProgressBlock',
    '../../../../../../../common/Navigation',
    '../../../../../../../common/Constants',
    'widgets/InfoPopup',
    'tablelib/Table',
    'tablelib/plugins/SortableHeader',
    'tablelib/plugins/Selection',
    './OperatorDetailsPaneRegionView',
    'template!./pomExample.html'
], function (_, binding, PaneRegion, ActionIcon, ActionLink, ProgressBlock, Navigation, Constants, InfoPopup, Table,
             SortableHeader, Selection, View, pomExampleTemplate) {
    'use strict';

    return PaneRegion.extend({
        /*jshint validthis:true*/

        View: View,

        init: function () {
            this.isExpanded = false;
            this.operatorId = null;
            this.publicMethodId = null;
            this.operatorModel = this.options.model;
        },

        onStart: function () {
            this.view.afterRender();

            this.publicMethodsTable = new Table({
                plugins: [
                    new SortableHeader(),
                    new Selection({
                        selectableRows: true
                    })
                ],
                modifiers: [
                    {name: 'striped'}
                ],
                columns: [
                    {title: 'Name', attribute: 'name', width: '45%', sortable: true},
                    {title: 'Component', attribute: 'component', width: '55%', sortable: true}
                ]
            });
            this.publicMethodsTable.attachTo(this.view.getPublicMethodsTableHolder());

            this.publicMethodsTable.addEventHandler('sort', function (sortMode, attribute) {
                this.publicMethodsTable.setData(getSortedData(this.operatorModel.toJSON().publicMethods, sortMode, attribute));
                markSelectedTableRow.call(this);
            }.bind(this));

            this.publicMethodsTable.addEventHandler('rowselect', function (row, isSelected) {
                this.getEventBus().publish(Constants.events.SHOW_PUBLIC_METHOD_DETAILS, row.options.model.id);
                var url = Navigation.getOperatorDetailsUrlWithParams(this.operatorId);
                if (isSelected) {
                    url = Navigation.getPublicMethodDetailsUrlWithParams(this.operatorId, row.options.model.id);
                }
                Navigation.navigateTo(url);
            }.bind(this));

            this.view.getOverlay().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getOperatorDetailsUrlWithParams(this.operatorId));
            }.bind(this));

            binding.bindModel(this.operatorModel, 'name', this.view.getNameHolder(), 'text');
            binding.bindModel(this.operatorModel, 'context', this.view.getContextHolder(), 'text');

            initPomExampleInfoPopup.call(this);
            this.operatorModel.addEventHandler('change', this.onModelChange.bind(this));

            initProgressBlock.call(this);
            initExpandIcon.call(this);
            initCloseIcon.call(this);
        },

        showIcons: function () {
            this.view.showCommandsBlock();
        },

        hideIcons: function () {
            this.view.hideCommandsBlock();
        },

        onOperatorModelFetch: function () {
            this.publicMethodsTable.setData(this.operatorModel.toJSON().publicMethods);
            markSelectedTableRow.call(this);
            this.hideProgressBlock();
        },

        onCloseIconClick: function () {
            Navigation.navigateTo(Navigation.getOperatorsUrl());
        },

        onModelChange: function () {
            var gav = this.operatorModel.getGav(),
                groupId = '',
                artifactId = '',
                version = '';

            if (!_.isUndefined(gav)) {
                groupId = gav.groupId;
                artifactId = gav.artifactId;
                version = gav.version;
            }

            this.view.getGroupId().setText(groupId);
            this.view.getArtifactId().setText(artifactId);
            this.view.getVersion().setText(version);

            setInfoPopupContent.call(this, groupId, artifactId, version);
        },

        onExpandCollapseIconClick: function () {
            var url = Navigation.getOperatorDetailsUrlWithParams(this.operatorId);
            if (!this.isExpanded) {
                url += '?expanded=true';
            }
            Navigation.navigateTo(url);
        },

        onShow: function (attributesObj, queryObj) {
            this.showProgressBlock();

            updatePageAttributes.call(this, attributesObj);
            this.isExpanded = (queryObj && queryObj.expanded === 'true');
            updateExpandCollapseIcon.call(this);
            loadPaneData.call(this);
        },

        onRedraw: function (attributesObj, queryObj) {
            var oldOperatorId = this.operatorId;

            updatePageAttributes.call(this, attributesObj);
            this.isExpanded = (queryObj && queryObj.expanded === 'true');
            updateExpandCollapseIcon.call(this);

            if (oldOperatorId !== attributesObj.operatorId) {
                this.showProgressBlock();
                loadPaneData.call(this);
            }
            markSelectedTableRow.call(this);
        },

        showProgressBlock: function () {
            this.progressBlock.attachTo(this.view.getOverlay());
            this.view.getOverlay().setModifier('showProgress');
        },

        hideProgressBlock: function () {
            this.progressBlock.detach();
            this.view.getOverlay().removeModifier('showProgress');
        },

        enableOverlay: function () {
            this.view.getOverlay().setModifier('show');
        },

        disableOverlay: function () {
            this.view.getOverlay().removeModifier('show');
        }

    });

    function setInfoPopupContent (groupId, artifactId, version) {
        this.pomInfoPopup.setContent(pomExampleTemplate({
            groupId: groupId,
            artifactId: artifactId,
            version: version
        }));
    }

    function markSelectedTableRow () {
        this.publicMethodsTable.unselectAllRows();

        if (this.publicMethodId) {
            this.publicMethodsTable.selectRows(function (row) {
                return row.getData().id === this.publicMethodId;
            }.bind(this));
            this.getEventBus().publish(Constants.events.SHOW_PUBLIC_METHOD_DETAILS, this.publicMethodId);
        }
    }

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

    function loadPaneData () {
        if (!this.operatorId) {
            this.operatorModel.clear();
        } else {
            this.operatorModel.setId(this.operatorId);
            this.operatorModel.fetch({
                reset: true,
                success: function () {
                    this.onOperatorModelFetch();
                }.bind(this)
            });
        }
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

    function initPomExampleInfoPopup() {
        this.pomInfoPopup = new InfoPopup({
            content: pomExampleTemplate({}),
            width: '400px'
        });
        this.pomInfoPopup.attachTo(this.view.getPomExampleInfo());
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

    function initProgressBlock () {
        this.progressBlock = new ProgressBlock();
        this.progressBlock.showProgress();
    }

});
