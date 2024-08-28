/*global define*/
define([
    'jscore/core',
    './ActionLinkView',
    '../actionIcon/ActionIcon'
], function (core, View, ActionIcon) {
    'use strict';

    return core.Widget.extend({

        View: View,

        onViewReady: function () {
            if (this.options.icon) {
                this.actionIcon = new ActionIcon(this.options.icon);
                this.actionIcon.attachTo(this.view.getIconHolder());
            }
            if (this.options.link.target) {
                this.setTarget(this.options.link.target);
            }
            this.setLinkText(this.options.link.text);
            this.setLinkTitle(this.options.link.title || this.options.link.text);

            if (this.options.url) {
                this.setUrl(this.options.url);
            } else if (this.options.action) {
                this.addEventAction('click', function (e) {
                    e.preventDefault();
                    this.options.action.apply(null, arguments);
                }.bind(this));
            }
        },

        setHidden: function (isHidden) {
            if (isHidden) {
                this.getElement().setModifier('hide', undefined, 'eaTR-ActionLink');
            } else {
                this.getElement().removeModifier('hide', 'eaTR-ActionLink');
            }
        },

        getActionIcon: function () {
            return this.actionIcon;
        },

        setTarget: function (target) {
            if (target) {
                this.view.getLink().setAttribute('target', target);
            } else {
                this.view.getLink().removeAttribute('target');
            }
        },

        setLinkText: function (text) {
            this.view.getLink().setText(text);
        },

        setLinkTitle: function (title) {
            this.getElement().setAttribute('title', title);
            if (this.actionIcon) {
                this.actionIcon.setTitle(title);
            }
        },

        setUrl: function (url) {
            this.view.getLink().setAttribute('href', url);
        },

        addEventAction: function (eventName, callBack, context) {
            this.view.getLink().addEventHandler(eventName, callBack, context);
            if (this.actionIcon) {
                this.actionIcon.addEventHandler(eventName, callBack, context);
            }
        }
    });


});
