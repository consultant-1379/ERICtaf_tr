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
    './OperatorsPaneRegionView',
    '../../../models/OperatorsCollection',
    '../../../../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../../../../common/widgets/searchFormWidget/SearchFormWidget'
], function (core, PaneRegion, Navigation, ProgressBlock, Constants, Table, SortableHeader, Selection, ExpandableRows,
             View, OperatorsCollection, ActionIcon, SearchFormWidget) {
    'use strict';

    return PaneRegion.extend({
        /*jshint validthis:true*/

        View: View,

        FIND_ALL_QUERY: '',

        init: function () {
            this.operatorId = null;
            this.operatorsCollection = new OperatorsCollection();
            this.isSearchShown = false;
        },

        onStart: function () {
            this.view.afterRender();

            this._eventBus = new core.EventBus();

            this.operatorsTable = new Table({
                plugins: [
                    new SortableHeader(),
                    new Selection({
                        selectableRows: true
                    })
                ],
                tooltips: true,
                columns: [
                    {title: 'Name', attribute: 'name', width: '40%', sortable: true},
                    {title: 'Context', attribute: 'context', width: '40%', sortable: true},
                    {title: 'Public Methods Count', attribute: 'publicMethodsCount', width: '20%', sortable: true}
                ]
            });
            this.operatorsTable.attachTo(this.view.getTableHolder());

            this.operatorsTable.addEventHandler('sort', function (sortMode, attribute) {
                this.operatorsTable.setData(getSortedData(this.operatorsCollection.toJSON(), sortMode, attribute));
                markSelectedTableRow.call(this);
            }.bind(this));

            this.operatorsTable.addEventHandler('rowselect', function (row, isSelected) {
                var url = Navigation.getOperatorsUrl();
                if (isSelected) {
                    url = Navigation.getOperatorDetailsUrlWithParams(row.options.model.id);
                }
                Navigation.navigateTo(url);
            }.bind(this));

            this.view.getOverlay().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getOperatorsUrl());
            });

            initProgressBlock.call(this);

            initSearchClearIcon.call(this);
            updateSearchClearIcon.call(this);
            addQuickSearchEventHandlers.call(this);
            addAdvancedSearchEventHandlers.call(this);
            createSearchWidget.call(this);

            this.operatorsCollection.addEventHandler('reset', this.onOperatorsCollectionReset, this);
            this.operatorsCollection.fetch({reset: true});
            this.showProgressBlock();
        },

        onShow: function (attributesObj) {
            this.operatorId = undefined;
            if (attributesObj) {
                this.operatorId = attributesObj[Constants.urls.OPERATOR_ID_PARAM];
            }
        },

        onRedraw: function (attributesObj) {
            this.operatorId = undefined;
            if (attributesObj) {
                this.operatorId = attributesObj[Constants.urls.OPERATOR_ID_PARAM];
            }
            markSelectedTableRow.call(this);
        },

        onOperatorsCollectionReset: function () {
            this.operatorsTable.setData(this.operatorsCollection.toJSON());
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
            this.operatorsCollection.setQueryParameters(queryString, isQuickSearch);
            this.operatorsCollection.fetch({reset: true});
        }

    });

    function markSelectedTableRow () {
        this.operatorsTable.unselectAllRows();

        if (this.operatorId) {
            this.operatorsTable.selectRows(function (row) {
                return row.getData().id === this.operatorId;
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

    function getFields() {
        return [
            {name: 'Operator Name', title: 'Operator Name', value: 'name'}
        ];
    }

});
