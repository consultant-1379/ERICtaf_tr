define([
    'jscore/core',
    'text!./TestwaresPaneRegion.html',
    'styles!./TestwaresPaneRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableHolder = element.find('.eaTR-TestwaresRegion-content');
            this.overlay = element.find('.eaTR-TestwaresMainRegion-paneOverlay');
            this.quickSearch = element.find('.eaTR-DefaultBar-quickSearch');
            this.searchButton = element.find('.eaTR-DefaultBar-searchButton');
            this.searchInput = element.find('.eaTR-DefaultBar-searchInput');
            this.searchClearIcon = element.find('.eaTR-DefaultBar-searchClearIcon');
            this.closeSearchButton = element.find('.eaTR-DefaultBar-closeSearchButton');
            this.advancedSearchButton = element.find('.eaTR-DefaultBar-advancedSearchButton');
            this.slidingPanels = element.find('.eaTR-TestwaresRegion-slidingPanels');
            this.searchContent = element.find('.eaTR-TestwaresRegion-searchContent');
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
            this.slidingPanels.setModifier('search', '', 'eaTR-TestwaresRegion-slidingPanels');
        },

        hideSearchPanel: function () {
            this.slidingPanels.removeModifier('search', 'eaTR-TestwaresRegion-slidingPanels');
        }

    });

});
