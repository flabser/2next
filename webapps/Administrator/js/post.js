App = Ember.Application.create();

App.Router.map(function() {
	this.route('post');
});

App.IndexRoute = Ember.Route.extend({
  model: function() {
    return ['red', 'yellow', 'blue'];
  }
});

App.PostRoute = Ember.Route.extend({
	  model: function() {
	    return ['blue'];
	  },
	  actions: {
	    click: function(){
	      alert('click');
	    }
	  }
	});                                 