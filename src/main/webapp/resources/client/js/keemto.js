//Backbone custom
$(document).ready(function() {  
  
   Event = Backbone.Model.extend({

    defaults: {
      content: "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur.",
      user: 'stnevex',
      providerId:'twitter'
    },

    initialize: function() {
      if (!this.get("content")) {
        this.set({"content": this.defaults.content});
      }
    }
  });
  
  EventCollection = Backbone.Collection.extend({
    model: Event,
    
    localStorage: new Store("events")
  });
  window.Events = new EventCollection;

  
  EventView = Backbone.View.extend({

    tagName: 'div',
    className: 'coreMsgItem',

    template: _.template($('#event-template').html()),

    initialize: function() {
      _.bindAll(this, 'render');
      this.model.bind('change', this.render);
      this.model.view = this;

    },

    render: function() {
      $(this.el).html(this.template(this.model.toJSON()));
      return this;
    },

  });
  
  //Login
  //------
  LoginStatus = Backbone.Model.extend({
    defaults: {
      user: "stnevex"
    },
    initialize: function() {
    }
  });
  
  //http://stackoverflow.com/questions/5097107/how-do-i-fetch-a-single-model-in-backbone
  LoginStatusCollection = Backbone.Collection.extend({
    model: Event,
    localStorage: new Store("logins")
  });
  window.Logins = new LoginStatusCollection;
  
  LoginView = Backbone.View.extend({
    tagName: 'li',
    
    template: _.template($('#login-template').html()),

    initialize: function() {
      _.bindAll(this, 'render');
      this.model.bind('change', this.render);
      this.model.view = this;
    },

    render: function() {
      $(this.el).html(this.template(this.model.toJSON()));
      return this;
    },

  });

  // The Application
  // ---------------
  ApplicationView = Backbone.View.extend({
  
    el: $("body"),

    events: {
      "click #toggleHeaderButton": "toggleHeader",
      "click #eventButton":"createEvent",
      "submit #loginForm ":"login",
    },
        
    initialize: function() {
      _.bindAll(this, 'render', 'addEvent','refreshLoginStatus');
      Events.bind('add', this.addEvent);
      Logins.bind('add', this.refreshLoginStatus);
      this.render();
    },

    render: function() {
     return this;
    },
    
   login: function(loginStatus) {
      Logins.create({user:"stnevex"});
    },
    
    createEvent: function(event ) {
      Events.create();
    },
    
    addEvent: function(event ) {
      var view = new EventView({model: event});
      var eventViewElement = view.render().el;
      this.$("#coreMsg").prepend(eventViewElement);
    },
    
    refreshLoginStatus: function(login) {
      var view = new LoginView({model: login});
      this.$("#loginForm").replaceWith(view.render().el);
    },
    
    toggleFlag:0,
    
    toggleHeader: function(event ) {
      var toggleHeaderButton = this.$('#toggleHeaderButton');
      $('#header').slideToggle(500);
      var flagStatus = this.toggleFlag++ % 2;
      if (flagStatus == 0) {
        toggleHeaderButton.html("Show Notifications");
      } else {
        toggleHeaderButton.html("Hide Notifications");
      }
    }
  });
  // Finally, we kick things off by creating the **App**.
  window.Application = new ApplicationView;
});
