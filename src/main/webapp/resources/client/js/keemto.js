/*
 * Copyright (C) 2010 Benoit Guerout <bguerout at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//Keemto UI Javascript
$(document).ready(function () {

    //Application
    //-----
    var App = {
        Views: {},
        Routers: {},
        Collections: {},
        Models: {},

        log: function (str) {
            console.log(str);
        },

        init: function () {
            this.activeSession = new App.Models.Session();

            new App.Routers.Authentication();
            new App.Routers.Main();
            new App.Routers.Connections();
            Backbone.history.start();

            new App.Views.Header();
            var events = new App.Collections.Events();
            events.fetch({
                success: function () {
                    new App.Views.Events({
                        collection: events
                    });
                },
                error: function (collection) {
                    App.notify({
                        type: "error",
                        message: "Error loading events."
                    });
                }
            });

            this.notifier = new App.Notifier();
            this.notifier.bind("notification:error", function (notification) {
                App.log("An error has occurred : " + notification.message);
                alert("An error has occurred : " + notification.message);
            });
        },

        notify: function (notification) {
            this.notifier.notify(notification);
        },

        //TODO move login handling to an Auth object
        login: function (login, password) {
            var self = this;
            $.ajax({
                type: "POST",
                url: "/api/login",
                data: {
                    login: login,
                    password: password
                },
                dataType: "json",
                success: function (response) {
                    if (response.loggedIn) {
                        var currentUserLogin = response.username;
                        self.activeSession.set({
                            login: currentUserLogin
                        });
                    } else {
                        App.notify({
                            type: "error",
                            message: "Authentication has failed for user: " + response.username
                        });
                    }
                },
                error: function (response) {
                    App.notify({
                        type: "error",
                        message: "Authentication has failed."
                    });
                }
            });
        }
    };
    _.extend(App, Backbone.Events);

    //Notifier for error handling
    App.Notifier = function (a) {
        a || (a = {});
        this.initialize && this.initialize(a)
    };
    _.extend(App.Notifier.prototype, Backbone.Events, {
        notify: function (notification) {
            this.trigger("notification:" + notification.type, notification);
        }
    });

    App.Models.Session = Backbone.Model.extend({

        defaults: {
            login: "",
            password: ""
        },

        isAuthenticated: function () {
            return Boolean(this.get("login"));
        }
    });

    App.Routers.Authentication = Backbone.Router.extend({
        routes: {
            "logout": "logout"
        },

        logout: function () {
            App.activeSession.clear();
            //TODO add redirection to #
        }

    });

    //Events
    //------
    App.Models.Event = Backbone.Model.extend({});

    App.Collections.Events = Backbone.Collection.extend({

        model: App.Models.Event,
        url: '/api/events'
    });

    App.Views.Event = Backbone.View.extend({
        tagName: 'div',
        className: 'coreMsgItem',
        template: _.template($('#event-template').html()),

        initialize: function () {
            _.bindAll(this, 'render');
            this.model.bind('change', this.render);
            this.model.view = this;
        },

        render: function () {
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }
    });


    App.Views.Events = Backbone.View.extend({

        el: $("#main"),

        events: {
            "click #createEventButton": "createEvent"
        },

        initialize: function () {
            _.bindAll(this, 'render', 'addEventView');
            this.collection.bind('add', this.addEventView);
            this.render();
        },

        render: function () {
            $(this.el).empty();
            $(this.el).append('<div class="wrap"></div>');
            this.$('.wrap').append('<button id="createEventButton">Create Event</button>');
            this.$('.wrap').append('<div id="coreMsg"></div>');
            _(this.collection.models).each(function (event) {
                this.addEventView(event);
            }, this);

            return this;
        },

        addEventView: function (event) {
            var eventElement = new App.Views.Event({
                model: event
            }).render().el;
            this.$("#coreMsg").prepend($(eventElement).fadeIn(2500));
        },

        createEvent: function (event) {
            this.collection.create();
            App.log("An dummy event has been created: " + event);
        }

    });

    //Connections
    //-----------
    App.Models.Connection = Backbone.Model.extend({

    });

    App.Collections.Connections = Backbone.Collection.extend({

        model: App.Models.Connection,
        url: '/api/connections'
    });

    App.Routers.Connections = Backbone.Router.extend({
        routes: {
            "connections": "view"
        },

        view: function () {
            var connections = new App.Collections.Connections();
            connections.fetch({
                success: function () {
                    new App.Views.Connections({
                        collection: connections
                    });
                },
                error: function (collection) {
                    App.notify({
                        type: "error",
                        message: "Error loading connections."
                    });
                }
            });
        }
    });

    App.Views.Connection = Backbone.View.extend({
        tagName: 'li',
        template: _.template($('#connection-template').html()),
        events: {
            "click a.delete": "revoke"
        },

        initialize: function () {
            _.bindAll(this, 'render');
            this.model.bind('change', this.render);
            this.model.bind('destroy', this.remove);
            this.model.view = this;
        },

        render: function () {
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        },

        revoke: function () {
            this.model.destroy({
                success: _.bind(function(model, response) {
                    $(this.el).fadeOut('slow', function() {
                        $(this).remove();
                    })
                }, this),
                error: function(model, response) {
                    App.notify({
                        type: "error",
                        message: "Unable to revoke access to this application."
                    });
                }});
            return false;
        }

    });

    App.Views.Connections = Backbone.View.extend({

        el: $("#panel .wrap"),

        initialize: function () {
            _.bindAll(this, 'render');
            this.render();
        },

        render: function () {
            $(this.el).empty();

            $(this.el).append('<div id="panelContent"><h1>Your connections</h1><span>You have allowed Keemto to access the following applications</span><ul></ul></div>');

            _(this.collection.models).each(function (connection) {
                var connectionElement = new App.Views.Connection({
                    model: connection
                }).render().el;
                this.$('#panelContent ul').append(connectionElement);
            }, this);

            $(this.el).append('<div id="panelButtons"></div>');
            $(this.el).append(new App.Views.ButtonConnection({buttonId:"twitter", buttonText:"Add Twitter Connection"}).render().el);
            $(this.el).append('<span class="buttonLarge"><a>Cancel</a></span>');

            return this;
        }
    });

    App.Views.ButtonConnection = Backbone.View.extend({
        tagName: 'span',
        className: 'buttonLarge',
        events: {
            "click a": "goToProviderAuthorizeUrl"
        },

        initialize: function (options) {
            _.bindAll(this, 'render');
            this.buttonText = options.buttonText;
            this.buttonId = options.buttonId;
        },

        goToProviderAuthorizeUrl: function () {
            return false;
        },

        render: function () {
            $(this.el).append('<a>' + this.buttonText + '</a>');
            return this;
        }
    });


    // Defaults
    // ---------------
    App.Routers.Main = Backbone.Router.extend({
        routes: {
            "": "main"
        },

        main: function () {
            new App.Views.InfoPanel();
        }
    });

    App.Views.Header = Backbone.View.extend({

        el: $('#navigation'),
        toggleFlag: 0,
        formTemplate: _.template($('#form-template').html()),
        authTemplate: _.template($('#authenticated-template').html()),

        events: {
            "click #panelButton": "togglePanelButton",
            "submit #loginNav form": "submitLoginForm"
        },

        initialize: function () {
            _.bindAll(this, 'render');
            App.activeSession.bind('change', this.render);
            this.render();
        },

        submitLoginForm: function () {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            App.login(login, password);
            return false;
        },

        togglePanelButton: function () {
            var self = this;
            var flagStatus = this.toggleFlag++ % 2;
            $('#panel').slideToggle(500, function () {
                if (flagStatus == 0) {
                    self.$('#panelButton').html("Show Notifications");
                } else {
                    self.$('#panelButton').html("Hide Notifications");
                }
            });
            return false;
        },

        render: function () {
            $(this.el).empty();
            if (App.activeSession.isAuthenticated()) {
                $(this.el).html(this.authTemplate(App.activeSession.toJSON()));
            } else {
                $(this.el).html(this.formTemplate());
            }

            return this;
        }
    });

    App.Views.InfoPanel = Backbone.View.extend({

        el: $("#panel .wrap"),

        initialize: function () {
            _.bindAll(this, 'render');
            this.render();
        },

        render: function () {
            $(this.el).empty();
            $(this.el).append('<div id="panelContent"><h1>What is Keemto ?</h1></div>');
            this.$('#panelContent').append('<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>');
            return this;
        }
    });


    App.init();
});