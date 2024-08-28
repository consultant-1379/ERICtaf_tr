define([
    'jscore/core',
    '../../../../../../../common/region/PaneRegion',
    '../../../../../../../common/Navigation',
    '../../../../../../../common/widgets/progressBlock/ProgressBlock',
    '../../../../../../../common/Constants',
    'tablelib/Table',
    'tablelib/plugins/SortableHeader',
    'tablelib/plugins/Selection',
    'tablelib/plugins/ExpandableRows',
    './TestwaresPaneRegionView',
    './VersionsContent/VersionsContent',
    './customcells/DateCell/DateCell',
    './customcells/OwnersCell/OwnersCell',
    '../../../models/TestwaresCollection',
    '../../../../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../../../../common/widgets/searchFormWidget/SearchFormWidget'
], function (core, PaneRegion, Navigation, ProgressBlock, Constants, Table, SortableHeader, Selection, ExpandableRows,
             View, VersionsContent, DateCell, OwnersCell, TestwaresCollection, ActionIcon, SearchFormWidget) {
    'use strict';

    return PaneRegion.extend({
        /*jshint validthis:true*/

        View: View,

        FIND_ALL_QUERY: '',

        init: function () {
            this.testwaresCollection = new TestwaresCollection();
            this.isSearchShown = false;
        },

        onStart: function () {
            this.view.afterRender();

            this._eventBus = new core.EventBus();

            this.testwaresTable = new Table({
                plugins: [
                    new SortableHeader(),
                    new Selection({
                        selectableRows: true
                    }),
                    new ExpandableRows({
                        content: VersionsContent,
                        args: {
                            eventBus: this._eventBus
                        }
                    })
                ],
                modifiers: [
                    {name: 'expandableStriped'}
                ],
                tooltips: true,
                columns: [
                    {title: 'Group Id', attribute: 'groupId', width: '20%', sortable: true},
                    {title: 'Artifact Id', attribute: 'artifactId', width: '20%', sortable: true},
                    {title: 'Version', attribute: 'version', width: '10%', sortable: true},
                    {title: 'TAF Version', attribute: 'tafVersion', width: '10%', sortable: true},
                    {title: 'Owners', attribute: 'owners', width: '15%', cellType: OwnersCell},
                    {title: 'Created at', attribute: 'publishedAt', width: '15%', sortable: true, cellType: DateCell},
                    {title: 'Test Steps Count', attribute: 'testStepsCount', width: '10%', sortable: true}
                ]
            });
            this.testwaresTable.attachTo(this.view.getTableHolder());

            this.testwaresTable.addEventHandler('sort', function (sortMode, attribute) {
                this.testwaresTable.setData(getSortedData(this.testwaresCollection.toJSON(), sortMode, attribute));
                markSelectedTableRow.call(this);
            }.bind(this));

            this.testwaresTable.addEventHandler('rowselect', function (row, isSelected) {
                var url = Navigation.getDefaultUrl();
                if (isSelected) {
                    url = Navigation.getTestwareDetailsUrlWithParams(row.options.model.id);
                }
                Navigation.navigateTo(url);
            }.bind(this));

            this.view.getOverlay().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getDefaultUrl());
            });

            initProgressBlock.call(this);

            initSearchClearIcon.call(this);
            updateSearchClearIcon.call(this);
            addQuickSearchEventHandlers.call(this);
            addAdvancedSearchEventHandlers.call(this);
            createSearchWidget.call(this);

            this.testwaresCollection.addEventHandler('reset', this.onTestwaresCollectionReset, this);
            this.testwaresCollection.fetch({reset: true});
            this.showProgressBlock();
        },

        onShow: function (attributesObj) {
            this.testwareId = undefined;
            if (attributesObj) {
                this.testwareId = attributesObj[Constants.urls.TESTWARE_ID_PARAM];
            }
        },

        onRedraw: function (attributesObj) {
            this.testwareId = undefined;
            if (attributesObj) {
                this.testwareId = attributesObj[Constants.urls.TESTWARE_ID_PARAM];
            }
            this._eventBus.publish(VersionsContent.SELECT_TESTWARE_VERSION, this.testwareId);
            markSelectedTableRow.call(this);
        },

        onTestwaresCollectionReset: function () {
            this.testwaresTable.setData(this.testwaresCollection.toJSON());
            markSelectedTableRow.call(this);
            this.hideProgressBlock();
        },

        showProgressBlock: function () {
            this.progressBlock.attachTo(this.view.getOverlay());
            this.view.getOverlay().setModifier('showProgress');
        },

        hideProgressBlock: function () {
            this.view.getOverlay().removeModifier('showProgress');
            this.progressBlock.detach();
        },

        enableOverlay: function () {
            this.view.getOverlay().setModifier('show');
        },

        disableOverlay: function () {
            this.view.getOverlay().removeModifier('show');
        },

        onSearchButtonClick: function (e) {
            e.preventDefault();
            var searchInput = this.view.getSearchInput();
            var searchValue = searchInput.getValue().trim();
            if (searchValue.length > 0) {
                searchInput.setValue(searchValue);
                this.runSearch(searchValue, true);
                this.searchWidget.clearCriteria();
            }
        },

        onSearchInputKeyDown: function (e) {
            var keyCode = e.originalEvent.keyCode || e.originalEvent.which;
            if (keyCode === 13) { // Enter
                this.view.getSearchButton().trigger('click');
                e.preventDefault();
            }
        },

        onClearSearchInput: function () {
            var searchInput = this.view.getSearchInput();
            searchInput.setValue('');
            updateSearchClearIcon.call(this);
            this.runSearch(this.FIND_ALL_QUERY, false);
        },

        onFilterButtonClick: function () {
            if (this.isSearchShown) {
                hideAdvancedSearch.call(this);
            } else {
                showAdvancedSearch.call(this);
            }
        },

        runSearch: function (queryString, isQuickSearch) {
            this.testwaresCollection.setQueryParameters(queryString, isQuickSearch);
            this.testwaresCollection.fetch({reset: true});
        }

    });

    function markSelectedTableRow () {
        this.testwaresTable.unselectAllRows();

        if (this.testwareId) {
            this.testwaresTable.selectRows(function (row) {
                return row.getData().id === this.testwareId;
            }.bind(this));
        }
    }

    function getSortedData (data, sortMode, sortAttr) {
        var sortNum = sortMode === 'asc' ? -1 : 1;
        data.sort(function (a, b) {
            var attributeA = a[sortAttr] == null ? '' : a[sortAttr];
            var attributeB = b[sortAttr] == null ? '' : b[sortAttr];

            if (attributeA < attributeB) {
                return sortNum;
            }
            if (attributeA > attributeB) {
                return -1 * sortNum;
            }
            return 0;
        });
        return data;
    }

    function initProgressBlock () {
        this.progressBlock = new ProgressBlock();
        this.progressBlock.showProgress();
    }

    function initSearchClearIcon () {
        this.searchClearIcon = new ActionIcon({
            iconKey: 'close',
            interactive: true,
            hide: true
        });
        this.searchClearIcon.attachTo(this.view.getSearchClearIcon());
    }

    function updateSearchClearIcon () {
        var searchInput = this.view.getSearchInput();
        var isEmpty = searchInput.getValue().length === 0;
        this.searchClearIcon.setHidden(isEmpty);
    }

    function createSearchWidget () {
        this.searchWidget = new SearchFormWidget({
            region: this,
            fields: getFields()
        });
        this.searchWidget.attachTo(this.view.getSearchContent());
    }

    function addQuickSearchEventHandlers () {
        this.view.getSearchButton().addEventHandler('click', this.onSearchButtonClick, this);
        this.view.getSearchInput().addEventHandler('keydown', this.onSearchInputKeyDown, this);
        this.view.getSearchInput().addEventHandler('input', updateSearchClearIcon, this);
        this.view.getSearchClearIcon().addEventHandler('click', this.onClearSearchInput, this);
    }

    function addAdvancedSearchEventHandlers () {
        this.view.getCloseSearchButton().addEventHandler('click', this.onFilterButtonClick, this);
        this.view.getAdvancedSearchButton().addEventHandler('click', this.onFilterButtonClick, this);
    }

    function showAdvancedSearch () {
        this.view.showSearchPanel();
        this.view.hideAdvancedSearchButton();
        this.view.showCloseSearchButton();
        this.view.hideQuickSearch();
        this.isSearchShown = true;
    }

    function hideAdvancedSearch () {
        this.view.hideSearchPanel();
        this.view.hideCloseSearchButton();
        this.view.showAdvancedSearchButton();
        this.view.showQuickSearch();
        this.isSearchShown = false;
    }

    function getFields () {
        return [
            {name: 'Group ID', title: 'Group ID', value: 'groupId'},
            {name: 'Artifact ID', title: 'Artifact ID', value: 'artifactId'},
            {name: 'Version', title: 'Version', value: 'version'},
            {name: 'Suite Name', title: 'Suite Name', value: 'suites'},
            {name: 'TAF Version', title: 'TAF Version', value: 'tafVersion'},
            {name: 'Javadoc location', title: 'Javadoc location', value: 'javaDocLocation'},
            {name: 'GIT location', title: 'GIT location', value: 'gitLocation'},
            {name: 'Owner', title: 'Owner', value: 'owner'},
            {name: 'Description', title: 'Description', value: 'description'},
            {name: 'Name', title: 'Name', value: 'name'},
            {name: 'Component', title: 'Component', value: 'component'}
        ];
    }

});
