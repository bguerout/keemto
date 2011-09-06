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
(function() {

    var root = this;

    root.Keemto = {
        // The top-level namespace. All public Keemto classes will be attached to this.
        Views: {},
        Routers: {},
        Collections: {},
        Models: {},

        log: function (str) {
            console.log(str);
        },

        init: function () {
            this.activeSession = new Keemto.Models.Session();

            new Keemto.Routers.Authentication();
            new Keemto.Routers.Main();
            new Keemto.Routers.Connections();
            Backbone.history.start();

            new Keemto.Views.Header({
                el: $('#navigation')
            });

            var events = new Keemto.Collections.Events();
            events.fetch({
                success: function () {
                    new Keemto.Views.Events({
                        el: $("#main"),
                        collection: events
                    });
                },
                error: function (collection) {
                    Keemto.notify({
                        type: "error",
                        message: "Error loading events."
                    });
                }
            });

            this.notifier = new Keemto.Notifier();
            this.notifier.bind("notification:error", function (notification) {
                Keemto.log("An error has occurred : " + notification.message);
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
                url: "api/login",
                data: {
                    j_username: login,
                    j_password: password
                },
                dataType: "json",
                success: function (response) {
                    if (response.loggedIn) {
                        var currentUserLogin = response.username;
                        self.activeSession.set({
                            login: currentUserLogin
                        });
                    } else {
                        Keemto.notify({
                            type: "error",
                            message: "Authentication has failed for user: " + response.username
                        });
                    }
                },
                error: function (response) {
                    Keemto.notify({
                        type: "error",
                        message: "Authentication has failed."
                    });
                }
            });
        }
    };
    _.extend(Keemto, Backbone.Events);

    //Notifier for error handling
    Keemto.Notifier = function (a) {
        a || (a = {});
        this.initialize && this.initialize(a)
    };
    _.extend(Keemto.Notifier.prototype, Backbone.Events, {
        notify: function (notification) {
            this.trigger("notification:" + notification.type, notification);
        }
    });

    Keemto.Models.Session = Backbone.Model.extend({

        defaults: {
            login: "",
            password: ""
        },

        isAuthenticated: function () {
            return Boolean(this.get("login"));
        }
    });

    Keemto.Routers.Authentication = Backbone.Router.extend({
        routes: {
            "logout": "logout"
        },

        logout: function () {
            Keemto.activeSession.clear();
            window.location = "";
        }

    });

    //Events
    //------
    Keemto.Models.Event = Backbone.Model.extend({});

    Keemto.Collections.Events = Backbone.Collection.extend({

        model: Keemto.Models.Event,
        url: 'api/events'
    });

    Keemto.Views.Event = Backbone.View.extend({
        tagName: 'div',
        className: 'coreMsgItem',

        initialize: function () {
            _.bindAll(this, 'render');
            this.template = _.template($('#event-template').html());
            this.model.bind('change', this.render);
            this.model.view = this;
        },

        render: function () {
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }
    });

    Keemto.Views.Events = Backbone.View.extend({

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
            var eventElement = new Keemto.Views.Event({
                model: event
            }).render().el;
            this.$("#coreMsg").prepend($(eventElement).fadeIn(2500));
        },

        createEvent: function (event) {
            this.collection.create();
            Keemto.log("An dummy event has been created: " + event);
        }
    });

    //Connections
    //-----------
    Keemto.Models.Connection = Backbone.Model.extend({});

    Keemto.Collections.Connections = Backbone.Collection.extend({

        model: Keemto.Models.Connection,
        url: 'api/connections'
    });

    Keemto.Routers.Connections = Backbone.Router.extend({
        routes: {
            "connections": "view"
        },

        view: function () {
            var panel = $("#panelContent");
            var connections = new Keemto.Collections.Connections();
            connections.fetch({
                success: function () {
                    panel.empty();
                    new Keemto.Views.Connections({
                        el: panel,
                        collection: connections
                    });
                },
                error: function (collection, response) {
                    Keemto.notify({
                        type: "error",
                        message: "Error loading connections."
                    });
                }
            });
        }
    });

    Keemto.Views.Connection = Backbone.View.extend({
        tagName: 'li',
        events: {
            "click a.delete": "revoke"
        },

        initialize: function () {
            _.bindAll(this, 'render');
            this.template = _.template($('#connection-template').html());
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
                    Keemto.notify({
                        type: "error",
                        message: "Unable to revoke access to this application."
                    });
                }});
            return false;
        }

    });

    Keemto.Views.Connections = Backbone.View.extend({

        initialize: function () {
            _.bindAll(this, 'render');
            this.render();
        },

        render: function () {
            $(this.el).append('<h1>Your connections</h1><span>You have allowed Keemto to access the following applications</span><ul></ul>');

            _(this.collection.models).each(function (connection) {
                var connectionElement = new Keemto.Views.Connection({
                    model: connection
                }).render().el;
                this.$('ul').append(connectionElement);
            }, this);

            $(this.el).append('<div id="panelButtons"></div>');
            this.$("#panelButtons").append(new Keemto.Views.ButtonConnection({buttonId:"twitter", buttonText:"Add Twitter Connection"}).render().el);
            this.$("#panelButtons").append(new Keemto.Views.ButtonConnection({buttonId:"yammer", buttonText:"Add Yammer Connection"}).render().el);
            this.$("#panelButtons").append('<span class="buttonLarge"><a>Cancel</a></span>');

            return this;
        }
    });

    Keemto.Views.ButtonConnection = Backbone.View.extend({
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
            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "api/connections/" + this.buttonId);
            $('#main').append(form);

            form.submit();
            return false;
        },

        render: function () {
            $(this.el).append('<a>' + this.buttonText + '</a>');
            return this;
        }
    });

    // Home
    // ---------------
    Keemto.Routers.Main = Backbone.Router.extend({
        routes: {
            "": "main"
        },

        main: function () {
            var panel = $("#panelContent");
            panel.empty();
            new Keemto.Views.InfoPanel({
                el: panel
            });
        }
    });

    Keemto.Views.Header = Backbone.View.extend({

        toggleFlag: 0,

        events: {
            "click #panelButton": "togglePanelButton",
            "submit #loginNav form": "submitLoginForm"
        },

        initialize: function () {
            _.bindAll(this, 'render');
            Keemto.activeSession.bind('change', this.render);
            this.formTemplate = _.template($('#form-template').html());
            this.authTemplate = _.template($('#authenticated-template').html());
            this.render();
        },

        submitLoginForm: function () {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            Keemto.login(login, password);
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
            if (Keemto.activeSession.isAuthenticated()) {
                $(this.el).html(this.authTemplate(Keemto.activeSession.toJSON()));
            } else {
                $(this.el).html(this.formTemplate());
            }

            return this;
        }
    });

    Keemto.Views.InfoPanel = Backbone.View.extend({

        initialize: function () {
            _.bindAll(this, 'render');
            this.render();
        },

        render: function () {
            $(this.el).append('<h1>What is Keemto ?</h1>');
            $(this.el).append('<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>');
            return this;
        }
    });


}).call(this);