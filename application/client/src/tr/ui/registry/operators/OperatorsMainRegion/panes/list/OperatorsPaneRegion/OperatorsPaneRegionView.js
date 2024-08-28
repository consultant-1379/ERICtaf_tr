define([
    'jscore/core',
    'text!./OperatorsPaneRegion.html',
    'styles!./OperatorsPaneRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableHolder = element.find('.eaTR-OperatorsRegion-content');
            this.overlay = element.find('.eaTR-OperatorsMainRegion-paneOverlay');
            this.quickSearch = element.find('.eaTR-DefaultBar-quickSearch');
            this.searchButton = element.find('.eaTR-DefaultBar-searchButton');
            this.searchInput = element.find('.eaTR-DefaultBar-searchInput');
            this.searchClearIcon = element.find('.eaTR-DefaultBar-searchClearIcon');
            this.closeSearchButton = element.find('.eaTR-DefaultBar-closeSearchButton');
            this.advancedSearchButton = element.find('.eaTR-DefaultBar-advancedSearchButton');
            this.slidingPanels = element.find('.eaTR-OperatorsRegion-slidingPanels');
            this.searchContent = element.find('.eaTR-OperatorsRegion-searchContent');
            this.hideCloseSearchButton();
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTableHolder: function () {
            return this.tableHolder;
        },

        getOverlay: function () {
            return this.overlay;
        },

        getSearchButton: function () {
            return this.searchButton;
        },

        getSearchInput: function () {
            return this.searchInput;
        },

        getSearchClearIcon: function () {
            return this.searchClearIcon;
        },

        getAdvancedSearchButton: function () {
            return this.advancedSearchButton;
        },

        getCloseSearchButton: function () {
            return this.closeSearchButton;
        },

        getSearchContent: function () {
            return this.searchContent;
        },

        showQuickSearch: function () {
            this.quickSearch.removeModifier('hidden', 'eaTR-DefaultBar-quickSearch');
        },

        hideQuickSearch: function () {
            this.quickSearch.setModifier('hidden', '', 'eaTR-DefaultBar-quickSearch');
        },

        hideAdvancedSearchButton: function () {
            this.advancedSearchButton.setModifier('hidden', '', 'eaTR-DefaultBar-advancedSearchButton');
        },

        showAdvancedSearchButton: function () {
            this.advancedSearchButton.removeModifier('hidden', 'eaTR-DefaultBar-advancedSearchButton');
        },

        hideCloseSearchButton: function () {
            this.closeSearchButton.setModifier('hidden', '', 'eaTR-DefaultBar-closeSearchButton');
        },

        showCloseSearchButton: function () {
            this.closeSearchButton.removeModifier('hidden', 'eaTR-DefaultBar-closeSearchButton');
        },

        showSearchPanel: function () {
            this.slidingPanels.setModifier('search', '', 'eaTR-OperatorsRegion-slidingPanels');
        },

        hideSearchPanel: function () {
            this.slidingPanels.removeModifier('search', 'eaTR-OperatorsRegion-slidingPanels');
        }

    });

});
