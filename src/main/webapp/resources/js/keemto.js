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

    root.Keemto =
        (function() {

            var notifier;
            var templates = {};
            var mainView;
            var views = [];
            var authService;

            var registerHandlebarsHelpers = function() {
                Handlebars.registerHelper('timeago', function(timestamp) {
                    return $.timeago(new Date(timestamp));
                });
            };

            var preCompileTemplates = function() {
                $("script[type='text/template']").each(function(index) {
                    var id = $(this).attr('id');
                    Keemto.log("A new template has been discovered: " + id);
                    templates[id] = Handlebars.compile($("#" + id).html());
                });
            };

            var setApplicationRenderingForNotifications = function() {
                notifier.bind("message", function(notification) {
                    var msg = notification.message;
                    Keemto.log("[" + notification.level.toUpperCase() + "] " + msg);
                    var alert = new Keemto.Views.Alert({message:msg, level:notification.level});
                    $('#alerts').append(alert.el);
                });

                notifier.bind("user:signin", function() {
                    var loginSuccess = new Keemto.Views.Alert({message:"Authentication successfull!", level:'success'});
                    var el = loginSuccess.el;
                    $('#alerts').append(el);
                });
            };

            var renderDefaultViews = function() {
                new Keemto.Views.TopBar({el:$('#topbar')}).render();
            };

            var prepareNotifier = function(){
                notifier = new Keemto.Notifier();
                setApplicationRenderingForNotifications();
            };

             var initRouters = function(){
                authService = new Keemto.Routers.Authentication({notifier: notifier});
                new Keemto.Routers.Home();
                Backbone.history.start();
            };

            return {
                // The top-level namespace. All public Keemto classes will be attached to this.
                Views:{},
                Routers:{},
                Collections:{},
                Models:{},

                init:function() {
                    registerHandlebarsHelpers();
                    preCompileTemplates();
                    prepareNotifier();
                    initRouters();
                    renderDefaultViews();
                    Keemto.log("Keemto Client UI has been successfully initialize.");
                },

                /*
                Helpers method with application scope
                */

                log:function(str) {
                    console.log(str);
                },

                message:function(notification) {
                    notifier.fireMessage(notification);
                },

                listen:function(notificationName, callback) {
                    notifier.bind(notificationName, callback);
                },

                login:function(login, password,onSuccess) {
                    authService.login(login, password,onSuccess);
                },

                getAuth:function() {
                    return authService;
                },

                renderTemplate:function(id, model) {
                    Keemto.log("Rendering template " + id + " with data " + model);
                    return templates[id](model);
                },

                goToView:function(view) {
                    if (mainView != null) {
                        mainView.remove();
                    }
                    mainView = view;
                    $('#main').append(mainView.el);
                },

                goToHash:function(hash) {
                    Backbone.history.navigate(hash, true);
                }
            };
        })();

    _.extend(Keemto, Backbone.Events);

    //Notifier for error handling
    Keemto.Notifier = function(a) {
        a || (a = {});
        this.initialize && this.initialize(a)
    };
    _.extend(Keemto.Notifier.prototype, Backbone.Events, {

        initialize:function(options) {
            _.bindAll(this, 'fireSignin');
        },

        fireSignin:function(loginInfo) {
            this.trigger('user:signin', loginInfo);
            this.trigger("user:updated", loginInfo);
        },

        fireMessage:function(notification) {
            this.trigger("message:" + notification.level, notification);
            this.trigger("message", notification);
        }
    });

    Keemto.Models.User = Backbone.Model.extend({

        defaults:{
            login:"anonymous"
        },

        isAuthenticated:function() {
            return Boolean(this.get("login") != 'anonymous');
        }
    });

    Keemto.Routers.Authentication = Backbone.Router.extend({

        routes:{
            "logout":"logout",
            "login":"showLoginForm"
        },

        initialize:function(options) {
            this.currentUser = new Keemto.Models.User();
            this.notifier = options.notifier;
            var loginInfo = {login: this.login, authenticated: this.isUserAuthenticated()};
            this.currentUser.bind('change:login', _.bind(this.notifier.fireSignin, this.notifier, loginInfo));
        },

        logout:function() {
            currentUser.clear();
            Keemto.goToHash("");
        },

        showLoginForm:function() {
            var view = new Keemto.Views.Login();
            Keemto.goToView(view);
        },

        login:function(login, password,onSuccess) {
            var self = this;
            $.ajax({
                type:"POST",
                url:"api/login",
                data:{
                    j_username:login,
                    j_password:password
                },
                dataType:"json",
                success:function(response) {
                    if (response.loggedIn) {
                        var currentUserLogin = response.username;
                        self.currentUser.set({login:currentUserLogin});
                        _.isUndefined(onSuccess)? Keemto.goToHash("#events"): onSuccess.call();
                    } else {
                        Keemto.message({level:"error", message:"Authentication has failed for user: " + response.username});
                    }
                },
                error:function(response) {
                    Keemto.message({level:"error", message:"Authentication has failed."});
                }
            });
        },

        isUserAuthenticated:function() {
            return this.currentUser.isAuthenticated();
        }
    });

    Keemto.Views.Login = Backbone.View.extend({

        tagName:'section',
        className:'block',

        events:{
            "submit form":"submitLoginForm"
        },

        initialize:function() {
            this.render();
        },

        submitLoginForm:function() {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            Keemto.getAuth().login(login, password, _.bind(function(){Keemto.goToHash("#accounts");},this));
            return false;
        },

        render:function() {
            $(this.el).html(Keemto.renderTemplate("login-template"));
            return this;
        }
    });

    //Events
    //------
    Keemto.Models.Event = Backbone.Model.extend({});

    Keemto.Collections.Events = Backbone.Collection.extend({
        model:Keemto.Models.Event,
        url:'api/events',
        comparator:function(event) {
            return event.get("timestamp");
        }
    });

    Keemto.Routers.Home = Backbone.Router.extend({

        routes:{
            "":"home",
            "events":"showEvents",
            "accounts":"showAccounts"
        },

        initialize:function() {
            _.bindAll(this, 'fetchLastEvents');
            //Events collections is held by router because it must be maintain even if view is destroy.
            this.events = new Keemto.Collections.Events();
            this.registerFetchingTask(10000);
        },

        home:function() {
            Keemto.log("Routing user to home hash");
            new Keemto.Views.Tips({el:$("#tips")}).render();
        },

        showEvents:function() {
            var view = new Keemto.Views.Events({collection:this.events});
            Keemto.goToView(view);
        },

        showAccounts:function() {
            Keemto.log("Routing user to accounts hash");
            var view = new Keemto.Views.Accounts();
            Keemto.goToView(view);
        },

        registerFetchingTask:function(delay) {
            window.setInterval(this.fetchLastEvents, delay);
        },

        fetchLastEvents:function() {
            var lastFetchedEvent = this.events.max(
                function(event) {
                    return event.attributes.timestamp;
                });

            var fetchOptions = {};
            var collectionHasAlreadyBeenFetched = !_.isUndefined(lastFetchedEvent);
            if (collectionHasAlreadyBeenFetched) {
                var lastTimestamp = lastFetchedEvent.attributes.timestamp;
                fetchOptions = {add:true, data:"timestamp=" + lastTimestamp}
            }
            Keemto.log("Trying to fetch events newer than :" + lastTimestamp);
            this.events.fetch(fetchOptions);
        }
    });

    Keemto.Views.Events = Backbone.View.extend({

        tagName:'section',
        className:'block',

        initialize:function() {
            _.bindAll(this, 'render', 'addEventView');
            this.collection.bind('add', this.showEventButton);
            this.collection.bind('reset', this.render);
            this.collection.fetch({
                error:function(collection) {
                    Keemto.message({level:"error", message:"Error loading events."});
                }
            });
        },

        addEventView:function(event) {
            var eventElement = new Keemto.Views.Event({model:event}).el;
            this.$("#events").prepend($(eventElement).fadeIn(500));
        },

        render:function() {
            $(this.el).html(Keemto.renderTemplate("events-section-template"));
            _(this.collection.models).each(function(event) {
                this.addEventView(event);
            }, this);

            return this;
        }
    });

    Keemto.Views.Event = Backbone.View.extend({
        tagName:'div',
        className:'event row',

        initialize:function() {
            _.bindAll(this, 'render', 'fadeLabel');
            this.render();
        },

        fadeLabel:function() {
            var element = this.$(".label");
            if (element.hasClass('fade')) {
                this.$(".label").removeClass('in');
                if (!$.support.transition) {
                    this.$(".label").remove();
                }
            }
            return this;
        },

        render:function() {
            var contentEl = Keemto.renderTemplate("event-template", this.model.toJSON());
            $(this.el).append(contentEl);
            window.setTimeout(this.fadeLabel, 10000);
            return this;
        }
    });

    //Accounts
    //-----------
    Keemto.Models.Account = Backbone.Model.extend({});

    Keemto.Collections.Accounts = Backbone.Collection.extend({

        model:Keemto.Models.Account,
        url:'api/accounts'
    });

    Keemto.Views.Accounts = Backbone.View.extend({

        tagName:'section',
        className:'block',

        events:{

        },

        initialize:function() {
            _.bindAll(this, 'render');
            this.collection = new Keemto.Collections.Accounts();
            this.collection.bind('reset', this.render);
            this.collection.fetch({
                error:function(collection, response) {
                    Keemto.message({level:"error", message:"Error loading accounts."});
                }
            });
        },

        configurePopover:function() {
            this.$("a[rel=popover]").popover({offset:10, animate:true}).click(function(e) {
                e.preventDefault();
            });
        },

        render:function() {
            $(this.el).html(Keemto.renderTemplate("accounts-section-template"));
            this.collection.each(function(account) {
                var accElement = new Keemto.Views.Account({model:account}).el;
                this.$('tbody').append(accElement);
            }, this);
            this.configurePopover();
            return this;
        }
    });

    Keemto.Views.Account = Backbone.View.extend({
        tagName:'tr',
        className:'odd',

        events:{
            "click button.danger":"revoke"
        },

        initialize:function() {
            _.bindAll(this, 'render', 'revoke', 'remove');
            this.model.bind('change', this.render, this);
            this.model.bind('destroy', this.remove, this);
            this.render();
        },

        revoke:function() {
            this.model.destroy({
                success: function() {
                    Keemto.message({message:"Account successfuly revoked!", level:'success'});
                },
                error:function(model, response) {
                    Keemto.message({level:"error", message:"Unable to revoke account."});
                }});
        },

        remove:function() {
            $(this.el).remove();
        },

        render:function() {
            var accountElement = Keemto.renderTemplate("account-template", this.model.toJSON());
            $(this.el).append(accountElement);
            return this;
        }
    });

    // Home
    // ---------------
    Keemto.Views.TopBar = Backbone.View.extend({

        toggleFlag:0,

        events:{
            "click #top-tips":"toggleTips",
            "submit form":"submitLoginForm",
            "click li":"activate"
        },

        initialize:function() {
            _.bindAll(this, 'render');
            Keemto.listen('user:updated', this.render);
        },

        submitLoginForm:function() {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            Keemto.getAuth().login(login, password);
            return false;
        },

        activate:function(event) {
            $(this.el).addClass('activate');
            return true;
        },

        toggleTips:function() {
            var self = this;
            var flagStatus = this.toggleFlag++ % 2;
            if (flagStatus == 0) {
                $('#tips').fadeOut(1000, function() {
                    self.$('#top-tips').html("Show Tips")
                });
            } else {
                $('#tips').fadeIn(2, function() {
                    self.$('#top-tips').html("Hide Tips")
                });
            }
            return false;
        },

        configureDropdown: function() {

            $("body").bind("click", function (e) {
                $('.dropdown-toggle').parent("li").removeClass("open");
            });
            this.$(".dropdown-toggle").click(function (e) {
                var $li = $(this).parent("li").toggleClass('open');
                return false;
            });
        },

        render:function() {
            $(this.el).empty();
            var isAuthenticated = Keemto.getAuth().isUserAuthenticated();
            var topbar = Keemto.renderTemplate("topbar-template", {authenticated:isAuthenticated});
            $(this.el).append(topbar);
            this.configureDropdown();
            return this;
        }
    });

    Keemto.Views.Tips = Backbone.View.extend({

        events:{
            "click #had-a-look":"showEvents"
        },

        initialize:function() {
            _.bindAll(this, 'showEvents');
        },

        showEvents:function() {
            this.$('#welcome-tip-signin').fadeOut(1000);
        },

        render:function() {
            var isAuthenticated = Keemto.getAuth().isUserAuthenticated();
            var tip = Keemto.renderTemplate("welcome-tip-template", {authenticated:isAuthenticated});
            $(this.el).append(tip);
            return this;
        }
    });

    Keemto.Views.Alert = Backbone.View.extend({

        tagName:'div',
        className:'alert-message fade in',

        initialize:function() {
            _.bindAll(this, 'render', 'close');
            this.render();
        },

        render:function() {
            var el = $(this.el);
            el.addClass(this.options.level);
            el.attr('data-alert', this.options.level);
            el.append('<a class="close" href="#">x</a><p>' + this.options.message + '</p>');
            window.setTimeout(this.close, 5000);
            return this;
        },

        close:function() {
            this.$('a').click();
        }
    });

}).call(this);
