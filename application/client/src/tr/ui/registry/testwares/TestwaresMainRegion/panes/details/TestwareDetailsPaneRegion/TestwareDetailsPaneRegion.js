/*global Date*/
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
    './TestwareDetailsPaneRegionView',
    '../../../common/OwnersWidget/OwnersWidget',
    'template!./pomExample.html'
], function (_, binding, PaneRegion, ActionIcon, ActionLink, ProgressBlock, Navigation, Constants, InfoPopup, Table,
             SortableHeader, Selection, View, OwnersWidget, pomExampleTemplate) {
    'use strict';

    return PaneRegion.extend({
        /*jshint validthis:true*/

        View: View,

        init: function () {
            this.isExpanded = false;
            this.testwareId = null;
            this.testwareModel = this.options.model;
        },

        onStart: function () {
            this.view.afterRender();

            this.testStepsTable = new Table({
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
                    {title: 'Name', attribute: 'name', width: '30%', sortable: true},
                    {title: 'Component', attribute: 'component', width: '35%', sortable: true},
                    {title: 'Description', attribute: 'description', width: '35%', sortable: true}
                ]
            });
            this.testStepsTable.attachTo(this.view.getTestStepsTableHolder());

            this.testStepsTable.addEventHandler('sort', function (sortMode, attribute) {
                this.testStepsTable.setData(getSortedData(this.testwareModel.toJSON().testSteps, sortMode, attribute));
                markSelectedTableRow.call(this);
            }.bind(this));

            this.testStepsTable.addEventHandler('rowselect', function (row, isSelected) {
                this.getEventBus().publish(Constants.events.SHOW_TEST_STEP_DETAILS, row.options.model.id);
                var url = Navigation.getTestwareDetailsUrlWithParams(this.testwareId);
                if (isSelected) {
                    url = Navigation.getTestStepDetailsUrlWithParams(this.testwareId, row.options.model.id);
                }
                Navigation.navigateTo(url);
            }.bind(this));

            this.view.getOverlay().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getTestwareDetailsUrlWithParams(this.testwareId));
            }.bind(this));

            binding.bindModel(this.testwareModel, 'groupId', this.view.getGroupId(), 'text');
            binding.bindModel(this.testwareModel, 'artifactId', this.view.getArtifactId(), 'text');
            binding.bindModel(this.testwareModel, 'version', this.view.getVersion(), 'text');
            binding.bindModel(this.testwareModel, 'tafVersion', this.view.getTafVersion(), 'text');
            binding.bindModel(this.testwareModel, 'description', this.view.getDescription(), 'text');
            binding.bindModel(this.testwareModel, 'suites', this.view.getSuites(), 'text');

            initJavaDocLocationLink.call(this);
            initGitLocationLink.call(this);
            initPomLocationLink.call(this);
            initPomExampleInfoPopup.call(this);

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

        onTestwareModelFetch: function () {
            this.testStepsTable.setData(this.testwareModel.toJSON().testSteps);
            this.hideProgressBlock();

            setOwnersList.call(this, this.testwareModel.getOwners());
            setPublishedDate.call(this, this.testwareModel.getPublishedAt());

            setLinkVisibility(this.testwareModel.getGitLocation(), this.gitLink);
            setLinkVisibility(this.testwareModel.getJavaDocLocation(), this.javaDocLink);
            setLinkVisibility(this.testwareModel.getPomLocation(), this.pomLink);
            setInfoPopupContent.call(this);
            markSelectedTableRow.call(this);
        },

        onCloseIconClick: function () {
            Navigation.navigateTo(Navigation.getDefaultUrl());
        },

        onExpandCollapseIconClick: function () {
            var url = Navigation.getTestwareDetailsUrlWithParams(this.testwareId);
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
            var oldTestwareId = this.testwareId;

            updatePageAttributes.call(this, attributesObj);
            this.isExpanded = (queryObj && queryObj.expanded === 'true');
            updateExpandCollapseIcon.call(this);

            if (oldTestwareId !== attributesObj.testwareId) {
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

    function setInfoPopupContent () {
        this.pomInfoPopup.setContent(pomExampleTemplate({
            groupId: this.testwareModel.getGroupId(),
            artifactId: this.testwareModel.getArtifactId(),
            version: this.testwareModel.getVersion()
        }));
    }

    function setPublishedDate (publishedDate) {
        var date = new Date(publishedDate);
        this.view.getCreatedAt().setText(date.toLocaleString());
    }

    function setOwnersList (ownersList) {
        if (this.ownersWidget) {
            this.ownersWidget.destroy();
            this.view.getOwners().setText('');
        }
        this.ownersWidget = new OwnersWidget({
            owners: ownersList
        });
        this.ownersWidget.attachTo(this.view.getOwners());
    }

    function markSelectedTableRow () {
        this.testStepsTable.unselectAllRows();

        if (this.testStepId) {
            this.testStepsTable.selectRows(function (row) {
                return row.getData().id === this.testStepId;
            }.bind(this));
            this.getEventBus().publish(Constants.events.SHOW_TEST_STEP_DETAILS, this.testStepId);
        }
    }

    function setLinkVisibility (urlString, linkEl) {
        linkEl.setUrl(urlString ? urlString : '');
        linkEl.setLinkText(urlString ? urlString : '');
        linkEl.setHidden(!urlString || urlString === '');
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
        if (!this.testwareId) {
            this.testwareModel.clear();
        } else {
            this.testwareModel.setId(this.testwareId);
            this.testwareModel.fetch({
                reset: true,
                success: function () {
                    this.onTestwareModelFetch();
                }.bind(this)
            });
        }
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

    function initJavaDocLocationLink () {
        this.javaDocLink = new ActionLink({
//            icon: {iconKey: 'externalApp', title: 'url'},
            link: {text: 'url', title: 'Java Doc Location', target: '_blank'}
        });
        this.javaDocLink.attachTo(this.view.getJavaDocLink());
        this.javaDocLink.setHidden(true);
    }

    function initGitLocationLink () {
        this.gitLink = new ActionLink({
//            icon: {iconKey: 'externalApp', title: 'url'},
            link: {text: 'url', title: 'Git Location', target: '_blank'}
        });
        this.gitLink.attachTo(this.view.getGitLink());
        this.gitLink.setHidden(true);
    }

    function initPomLocationLink () {
        this.pomLink = new ActionLink({
//            icon: {iconKey: 'externalApp'},
            link: {text: 'url', title: 'POM Location', target: '_blank'}
        });
        this.pomLink.attachTo(this.view.getPomLink());
        this.pomLink.setHidden(true);
    }

    function initPomExampleInfoPopup() {
        this.pomInfoPopup = new InfoPopup({
            content: pomExampleTemplate({
                groupId: this.testwareModel.getGroupId(),
                artifactId: this.testwareModel.getArtifactId(),
                version: this.testwareModel.getVersion()
            }),
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
