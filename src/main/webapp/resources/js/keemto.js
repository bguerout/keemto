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
(function () {

    var root = this;

    root.Keemto =
        (function () {

            var user;
            var notifier;
            var templates = {};

            var registerHandlebarsHelpers = function () {
                Handlebars.registerHelper('timeago', function (timestamp) {
                    return $.timeago(new Date(timestamp));
                });
            };

            var preCompileTemplates = function () {
                $("script[type='text/template']").each(function (index) {
                    var id = $(this).attr('id');
                    Keemto.log("A new template has been discovered: " + id);
                    templates[id] = Handlebars.compile($("#" + id).html());
                });
            };

            var renderDefaultViews = function () {
                $("#topbar").append(new Keemto.Common.TopBarView({model:user}).render().el);
            };

            var prepareNotifier = function () {
                user.bind('change:login', _.bind(notifier.fire, notifier, "user:updated", user));

                notifier.bind("message", function (notification) {
                    var msg = notification.message;
                    Keemto.log("[" + notification.level.toUpperCase() + "] " + msg);
                    var alert = new Keemto.Common.AlertView({message:msg, level:notification.level});
                    $('#alerts').append(alert.el);
                });

                notifier.bind("user:signin", function () {
                    var loginSuccess = new Keemto.Common.AlertView({message:"Authentication successfull!", level:'success'});
                    var el = loginSuccess.el;
                    $('#alerts').append(el);
                });

                $('body').ajaxError(function (e, jqxhr, settings, exception) {
                    Keemto.log(e, jqxhr, settings, exception);
                });
            };

            var initRouters = function () {
                new Keemto.Common.Routers({user:user});
                new Keemto.Manage.Routers({user:user});
                new Keemto.Timeline.Routers();
                Backbone.history.start();
            };

            return {
                // The top-level namespace. All public Keemto classes will be attached to this.
                Common:{},
                Timeline:{},
                Manage:{},

                init:function (options) {
                    options || (options = {user:new Keemto.Common.User()});
                    user = options.user;
                    notifier = new Keemto.Common.Notifier();
                    prepareNotifier();
                    registerHandlebarsHelpers();
                    preCompileTemplates();
                    initRouters();
                    renderDefaultViews();
                    Keemto.log("Keemto Client UI has been successfully initialize.");
                },

                /*
                 Helpers method with application scope
                 */

                log:function (str) {
                    console.log(str);
                },

                message:function (notification) {
                    notifier.fireMessage(notification);
                },

                listen:function (notificationName, callback) {
                    notifier.bind(notificationName, callback);
                },

                authenticate:function (login, password, onSuccess) {
                    $.ajax({
                        type:"POST",
                        url:"api/login",
                        data:{
                            j_username:login,
                            j_password:password
                        },
                        dataType:"json",
                        success:function (response) {
                            if (response.loggedIn) {
                                var currentUserLogin = response.username;
                                user.set({login:currentUserLogin});
                                notifier.fire("user:signin");
                                _.isUndefined(onSuccess) ? Keemto.navigateToHash("#timeline") : onSuccess.call();
                            } else {
                                Keemto.message({level:"error", message:"Authentication has failed for user: " + response.username});
                            }
                        },
                        error:function (response) {
                            Keemto.message({level:"error", message:"Authentication has failed."});
                        }
                    });
                },

                renderTemplate:function (id, model) {
                    Keemto.log("Rendering template " + id + " with data " + model);
                    return templates[id](model);
                },

                showView:function (elementId, newView) {
                    $(elementId).empty();
                    //TODO check if a view already exists and remove it
                    $(elementId).append(newView.el);
                },

                renderPage:function (views) {
                    if (_.isUndefined(views.header)) {
                        views.header = new Keemto.Common.EmptyView();
                    }
                    if (_.isUndefined(views.main)) {
                        views.main = new Keemto.Common.EmptyView();
                    }
                    this.showView('#header', views.header);
                    this.showView('#main', views.main);
                },

                navigateToHash:function (hash) {
                    Backbone.history.navigate(hash, true);
                },

                isUserAuthenticated:function () {
                    return !user.isAnonymous();
                }
            };
        })();

    _.extend(Keemto, Backbone.Events);

    /*---------------------------------------------------------------------------------------------------------------
     COMMON
     ---------------------------------------------------------------------------------------------------------------*/
    Keemto.Common.User = Backbone.Model.extend({

        defaults:{
            login:"anonymous"
        },

        // Return a copy of the model's `attributes` object.
        toJSON:function () {
            var cloned = _.clone(this.attributes);
            cloned.isAnonymous = this.isAnonymous();
            return cloned;
        },

        isAnonymous:function () {
            return (this.get("login") == 'anonymous');
        }
    });

    Keemto.Common.Notifier = function (a) {
        a || (a = {});
        this.initialize && this.initialize(a)
    };
    _.extend(Keemto.Common.Notifier.prototype, Backbone.Events, {

        initialize:function (options) {
            _.bindAll(this, 'fire');
        },

        fire:function (notification, data) {
            this.trigger(notification, data);
        },

        fireMessage:function (notification) {
            this.trigger("message:" + notification.level, notification);
            this.trigger("message", notification);
        }
    });

    Keemto.Common.Account = Backbone.Model.extend({});

    Keemto.Common.Event = Backbone.Model.extend({});

    Keemto.Common.Routers = Backbone.Router.extend({

        routes:{
            "":"showHome",
            "login":"showLoginForm"
        },

        initialize:function (options) {
            this.user = options.user;
        },

        showHome:function () {
            Keemto.log("Routing user to home.");
            Keemto.renderPage({
                header:new Keemto.Common.WelcomeView()
            });
        },

        showLoginForm:function () {
            Keemto.renderPage({main:new Keemto.Common.LoginView()});
        }
    });

    /*---------------------------------------------------------------------------------------------------------------
     TIMELINE
     ---------------------------------------------------------------------------------------------------------------*/

    Keemto.Timeline.Routers = Backbone.Router.extend({

        routes:{
            "timeline":"showTimeline"
        },

        initialize:function (options) {
            _.bindAll(this, '_fetchLastEvents');
            //Events collections is held by router because it must be maintain even if view is destroy.
            this.events = new Keemto.Timeline.Events({name:"Xebia's Timeline"});
            this._registerFetchingTask(60000);
        },

        showTimeline:function () {
            var randomAccounts = new Keemto.Timeline.RandomAccounts();
            randomAccounts.fetch();
            var timeline = new Keemto.Timeline.View({collection:this.events, randomAccounts:randomAccounts});
            Keemto.renderPage({main:timeline});
        },

        _registerFetchingTask:function (delay) {
            window.setInterval(this._fetchLastEvents, delay);
        },

        _fetchLastEvents:function () {
            var lastFetchedEvent = this.events.max(
                function (event) {
                    return event.attributes.timestamp;
                });

            var fetchOptions = {};
            var collectionHasAlreadyBeenFetched = !_.isUndefined(lastFetchedEvent);
            if (collectionHasAlreadyBeenFetched) {
                var lastTimestamp = lastFetchedEvent.attributes.timestamp;
                var fetchOptions = {add:true, data:"newerThan=" + lastTimestamp}
            }
            Keemto.log("Trying to fetch events newer than :" + lastTimestamp);
            this.events.fetch(fetchOptions);
        }
    });


    Keemto.Timeline.Events = Backbone.Collection.extend({

        model:Keemto.Common.Event,
        url:'api/events',

        initialize:function (options) {
            this.name = options.name;
        },

        //override
        comparator:function (event) {
            return event.get("timestamp");
        }
    });

    Keemto.Timeline.RandomAccounts = Backbone.Collection.extend({

        model:Keemto.Common.Account,
        url:'api/accounts/random'
    });

    Keemto.Timeline.View = Backbone.View.extend({

        tagName:'div',
        className:'content',
        fetchedEvents:[],

        events:{
            "click #show-fetched-button":"_addFetchedEvents"
        },

        initialize:function (options) {
            _.bindAll(this, 'render', '_addEvent', '_addAccount', '_showFetchedButton');
            this.collection.bind('add', this.showFetchedButton);
            this.collection.bind('reset', this.render);
            this.collection.fetch({
                error:function (collection) {
                    Keemto.message({level:"error", message:"Error loading events."});
                }
            });

            this.randomAccounts = this.options.randomAccounts;
            this.randomAccounts.bind('reset', this.render);
        },

        render:function () {
            $(this.el).html(Keemto.renderTemplate("timeline-section-template", {"name":this.collection.name}));
            _(this.collection.models).each(function (event) {
                this._addEvent(event);
            }, this);

            _(this.randomAccounts.models).each(function (account) {
                this._addAccount(account);
            }, this);
            $("a[rel=twipsy]").twipsy({live:true});

            this._renderTips();
            return this;
        },

        _showFetchedButton:function (model) {
            this.fetchedEvents.push(model);
            $('title').text('(' + this.fetchedEvents.length + ')Keemto');
            this.$("#show-fetched-button").html(this.fetchedEvents.length + ' new events');
            this.$("#show-fetched-button").show();
        },

        _addFetchedEvents:function () {
            this.$("#show-fetched-button").hide();
            $('title').text("Keemto");
            _.each(this.fetchedEvents, this.addEvent);
        },

        _addEvent:function (event) {
            var eventElement = new Keemto.Timeline.EventView({model:event}).el;
            this.$("#events").prepend($(eventElement).fadeIn(1000));
        },

        _addAccount:function (account) {
            var contentEl = Keemto.renderTemplate("random-account-template", account.toJSON());
            this.$("#random-accounts .media-grid").append(contentEl);
        },

        _renderTips:function renderTips() {
            $("a[rel=popover]").popover({offset:10, animate:true, placement:"below"}).click(function (e) {
                e.preventDefault();
            });
        }
    });

    Keemto.Timeline.EventView = Backbone.View.extend({
        tagName:'div',
        className:'event row',

        initialize:function () {
            _.bindAll(this, 'render', '_fadeLabel');
            this.render();
        },

        render:function () {
            var contentEl = Keemto.renderTemplate("event-template", this.model.toJSON());
            $(this.el).append(contentEl);
            window.setTimeout(this.fadeLabel, 8000);
            return this;
        },

        _fadeLabel:function () {
            var element = this.$(".label");
            if (element.hasClass('fade')) {
                this.$(".label").removeClass('in');
                if (!$.support.transition) {
                    this.$(".label").remove();
                }
            }
            return this;
        }
    });

    /*---------------------------------------------------------------------------------------------------------------
     MANAGE
     ---------------------------------------------------------------------------------------------------------------*/

    Keemto.Manage.Routers = Backbone.Router.extend({

        routes:{
            "accounts":"showAccounts"
        },

        initialize:function (options) {
            this.user = options.user;
        },

        showAccounts:function () {
            Keemto.log("Routing user to accounts hash");
            var userAccounts = new Keemto.Manage.UserAccounts({user:this.user});
            var view = new Keemto.Manage.View({collection:userAccounts});
            Keemto.renderPage({main:view});
        }

    });

    Keemto.Manage.UserAccounts = Backbone.Collection.extend({

        model:Keemto.Common.Account,

        initialize:function (options) {
            this.login = options.user.get("login");
        },

        url:function () {
            return 'api/users/' + this.login + "/accounts";
        }
    });

    Keemto.Manage.View = Backbone.View.extend({

        tagName:'div',
        className:'content accounts',

        initialize:function () {
            _.bindAll(this, 'render');
            this.collection.bind('reset', this.render);
            this.collection.fetch({
                error:function (collection, response) {
                    Keemto.message({level:"error", message:"Error loading accounts."});
                }
            });
        },

        render:function () {
            $(this.el).html(Keemto.renderTemplate("manageaccounts-section-template"));
            this.collection.each(function (account) {
                var accElement = new Keemto.Manage.AccountView({model:account}).el;
                this.$('tbody').append(accElement);
            }, this);

            this.$('#accounts').append(new Keemto.Manage.ProviderButton({id:"twitter", className:'btn primary'}).render().el);
            this.$('#accounts').append('<span>&nbsp;</span>');
            this.$('#accounts').append(new Keemto.Manage.ProviderButton({id:"yammer", className:'btn success'}).render().el);
            return this;
        }
    });

    Keemto.Manage.AccountView = Backbone.View.extend({
        tagName:'tr',
        className:'odd',

        events:{
            "click button.danger":"_revoke"
        },

        initialize:function () {
            _.bindAll(this, 'render', '_revoke', '_remove');
            this.model.bind('change', this.render, this);
            this.model.bind('destroy', this.remove, this);
            this.render();
        },

        render:function () {
            var accountElement = Keemto.renderTemplate("revokable-account-template", this.model.toJSON());
            $(this.el).append(accountElement);
            return this;
        },

        _revoke:function () {
            this.model.destroy({
                success:function () {
                    Keemto.message({message:"Account successfuly revoked!", level:'success'});
                },
                error:function (model, response) {
                    Keemto.message({level:"error", message:"Unable to revoke account."});
                }});
        },

        _remove:function () {
            $(this.el).remove();
        }
    });

    Keemto.Manage.ProviderButton = Backbone.View.extend({
        tagName:'a',
        events:{
            "click":"_connect"
        },

        initialize:function (options) {
            _.bindAll(this, 'render');
        },

        render:function () {
            $(this.el).text("Add " + this.id + " account");
            return this;
        },

        _connect:function () {
            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "connect/" + this.id);
            $('body').append(form);
            form.submit();
            return false;
        }
    });

    Keemto.Common.OAuthConfirmPopup = Backbone.View.extend({

        initialize:function () {
            _.bindAll(this, 'render');
            this.providerId = 'yammer';
        },

        render:function () {
            $('#oauth-confirm').modal({show:true, backdrop:true});
            $('#oauth-confirm-button').click(function () {
                $('#oauth-confirm-form').submit();
                Keemto.log("OAuth Verifier Confirmation has been submitted for " + this.providerId);
            });
        }
    });

    /*---------------------------------------------------------------------------------------------------------------
     VIEWS
     ---------------------------------------------------------------------------------------------------------------*/
    Keemto.Common.LoginView = Backbone.View.extend({

        tagName:'div',
        className:'content',

        events:{
            "submit form":"_submitLoginForm"
        },

        initialize:function () {
            this.render();
        },

        render:function () {
            $(this.el).html(Keemto.renderTemplate("login-template"));
            return this;
        },

        _submitLoginForm:function () {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            Keemto.authenticate(login, password, _.bind(function () {
                Keemto.navigateToHash("#accounts");
            }, this));
            return false;
        }
    });

    Keemto.Common.TopBarView = Backbone.View.extend({

        tagName:'div',

        events:{
            "submit form":"_submitLoginForm",
            "click li":"_activate"
        },

        initialize:function () {
            _.bindAll(this, 'render');
            Keemto.listen('user:updated', this.render);
        },

        render:function () {
            $(this.el).empty();
            var topbar = Keemto.renderTemplate("topbar-template", this.model.toJSON());
            $(this.el).append(topbar);
            this._configureDropdown();
            return this;
        },

        _submitLoginForm:function () {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            Keemto.authenticate(login, password);
            return false;
        },

        _activate:function (event) {
            $(this.el).addClass('activate');
            return true;
        },

        _configureDropdown:function () {

            $("body").bind("click", function (e) {
                $('.dropdown-toggle').parent("li").removeClass("open");
            });
            this.$(".dropdown-toggle").click(function (e) {
                var $li = $(this).parent("li").toggleClass('open');
                return false;
            });
        }
    });

    Keemto.Common.EmptyView = Backbone.View.extend({
        tagName:'div',
        render:function () {
            return this;
        }
    });

    Keemto.Common.WelcomeView = Backbone.View.extend({

        tagName:'div',

        events:{
            "submit form":"_submitLoginForm"
        },

        render:function () {
            var isAuthenticated = Keemto.isUserAuthenticated();
            var tip = Keemto.renderTemplate("welcome-template", {authenticated:isAuthenticated});
            $(this.el).append(tip);
            return this;
        },

        initialize:function () {
            this.render();
        },

        _submitLoginForm:function () {
            var login = this.$('input[name="login"]').val();
            var password = this.$('input[name="password"]').val();
            Keemto.authenticate(login, password, _.bind(function () {
                $(this.el).remove();
                Keemto.navigateToHash("#accounts");
            }, this));
            return false;
        }
    });

    Keemto.Common.AlertView = Backbone.View.extend({

        tagName:'div',
        className:'alert-message fade in',

        initialize:function () {
            _.bindAll(this, 'render', '_close');
            this.render();
        },

        render:function () {
            var el = $(this.el);
            el.addClass(this.options.level);
            el.attr('data-alert', this.options.level);
            el.append('<a class="close" href="#">x</a><p>' + this.options.message + '</p>');
            window.setTimeout(this.close, 4000);
            return this;
        },

        _close:function () {
            this.$('a').click();
        }
    });


}).call(this);
