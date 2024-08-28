define([
    'jscore/core',
    'text!./TestwareDetailsPaneRegion.html',
    'styles!./TestwareDetailsPaneRegion.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.overlay = element.find('.eaTR-TestwaresMainRegion-paneOverlay');
            this.commandsHolder = element.find('.ebLayout-HeadingCommands');
            this.commandsBlock = element.find('.ebLayout-HeadingCommands-block');
            this.iconsHolder = element.find('.ebLayout-HeadingCommands-iconHolder');
            this.testStepsTableHolder = element.find('.eaTR-TestwareDetailsPane-testStepsTable');
            this.groupId = element.find('.eaTR-TestwareDetailsPane-groupId');
            this.artifactId = element.find('.eaTR-TestwareDetailsPane-artifactId');
            this.version = element.find('.eaTR-TestwareDetailsPane-version');
            this.tafVersion = element.find('.eaTR-TestwareDetailsPane-taf-version');
            this.owners = element.find('.eaTR-TestwareDetailsPane-owners');
            this.suites = element.find('.eaTR-TestwareDetailsPane-suites');
            this.createdAt = element.find('.eaTR-TestwareDetailsPane-createdAt');
            this.javaDocLink = element.find('.eaTR-TestwareDetailsPane-javaDocLink');
            this.gitLink = element.find('.eaTR-TestwareDetailsPane-gitLink');
            this.pomLink = element.find('.eaTR-TestwareDetailsPane-pomLink');
            this.pomExampleInfo = element.find('.eaTR-TestwareDetailsPane-pomExampleInfo');
            this.description = element.find('.eaTR-TestwareDetailsPane-description');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        },

        showCommandsBlock: function () {
            this.commandsHolder.append(this.commandsBlock);
        },

        hideCommandsBlock: function () {
            this.commandsBlock.detach();
        },

        getOverlay: function () {
            return this.overlay;
        },

        getIconsHolder: function () {
            return this.iconsHolder;
        },

        getTestStepsTableHolder: function () {
            return this.testStepsTableHolder;
        },

        getGroupId: function () {
            return this.groupId;
        },

        getArtifactId: function () {
            return this.artifactId;
        },

        getOwners: function () {
            return this.owners;
        },

        getCreatedAt: function () {
            return this.createdAt;
        },

        getVersion: function () {
            return this.version;
        },

        getTafVersion: function () {
            return this.tafVersion;
        },

        getJavaDocLink: function () {
            return this.javaDocLink;
        },

        getGitLink: function () {
            return this.gitLink;
        },

        getPomLink: function () {
            return this.pomLink;
        },

        getPomExampleInfo: function () {
            return this.pomExampleInfo;
        },

        getDescription: function () {
            return this.description;
        },

        getSuites: function () {
            return this.suites;
        }
    });

});
