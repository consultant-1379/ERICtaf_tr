define([
    'jscore/core',
    'text!./OperatorDetailsPaneRegion.html',
    'styles!./OperatorDetailsPaneRegion.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.overlay = element.find('.eaTR-OperatorsMainRegion-paneOverlay');
            this.commandsHolder = element.find('.ebLayout-HeadingCommands');
            this.commandsBlock = element.find('.ebLayout-HeadingCommands-block');
            this.iconsHolder = element.find('.ebLayout-HeadingCommands-iconHolder');
            this.nameHolder = element.find('.eaTR-OperatorDetailsPane-name');
            this.contextHolder = element.find('.eaTR-OperatorDetailsPane-context');
            this.groupId = element.find('.eaTR-OperatorDetailsPane-groupId');
            this.artifactId = element.find('.eaTR-OperatorDetailsPane-artifactId');
            this.version = element.find('.eaTR-OperatorDetailsPane-version');
            this.pomExampleInfo = element.find('.eaTR-OperatorDetailsPane-pomExampleInfo');
            this.publicMethodsTableHolder = element.find('.eaTR-OperatorDetailsPane-publicMethodsTable');
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

        getNameHolder: function () {
            return this.nameHolder;
        },

        getContextHolder: function () {
            return this.contextHolder;
        },

        getGroupId: function () {
            return this.groupId;
        },

        getArtifactId: function () {
            return this.artifactId;
        },

        getVersion: function () {
            return this.version;
        },

        getPomExampleInfo: function () {
            return this.pomExampleInfo;
        },

        getPublicMethodsTableHolder: function () {
            return this.publicMethodsTableHolder;
        }
    });

});
