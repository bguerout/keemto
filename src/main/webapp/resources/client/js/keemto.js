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
                    var currentUserLogin = response.login;
                    self.activeSession.set({
                        login: currentUserLogin
                    });
                    self.log("User " + currentUserLogin + " has been authenticated ");
                },
                error: function (response) {
                    alert(response.responseText);
                }
            });
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
                    alert("Error loading events.");
                }
            });


        }
    };
    _.extend(App, Backbone.Events);

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
        }

    });

    //Events
    //------
    App.Models.Event = Backbone.Model.extend({});

    App.Collections.Events = Backbone.Collection.extend({

        model: App.Models.Event,
        url: '/events'
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
    App.Models.Connection = Backbone.Model.extend({});

    App.Collections.Connections = Backbone.Collection.extend({

        model: App.Models.Connection,
        url: '/connections'
    });

    App.Routers.Connections = Backbone.Router.extend({
        routes: {
            "connections": "connections"
        },

        connections: function () {
            var connections = new App.Collections.Connections();
            connections.fetch({
                success: function () {
                    new App.Views.Connections({
                        collection: connections
                    });
                },
                error: function (collection) {
                    alert("Error loading connections.");
                }
            });
        }
    });

    App.Views.Connection = Backbone.View.extend({
        tagName: 'div',
        template: _.template($('#connection-template').html()),

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
                var connectionElement = new App.Views.Connection({model: connection}).render().el;
                this.$('#panelContent ul').append(connectionElement);
            }, this);

            $(this.el).append('<div id="panelButtons"></div>');
            $(this.el).append('<span class="buttonLarge"><a href="#" id="addConnection">Add Connection</a>');
            $(this.el).append('<span class="buttonLarge"><a href="#" id="cancelConnection">Cancel</a>');

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
            "click #connectionsButton": "togglePanelButton",
            "click #connectionsButton": "openConnectionsManager",
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
            $('#panel').slideToggle(500,function(){
                if (flagStatus == 0) {
                    self.$('#panelButton').html("Show Notifications");
                } else {
                    self.$('#panelButton').html("Hide Notifications");
                }
            });
            return false;
        },

        openConnectionsManager: function(){
              this.to
        },

        submitLoginForm: function () {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            App.login(login, password);
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