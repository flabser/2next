MyApp = Ember.Application.create();

var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});

var baseURL = host.extend({
    namespace: 'Administrator/rest'
});

MyApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
        	console.log("type=" + type);
            return 'session';

    }
});


MyApp.Router.map(function(){
	this.route('login',{path: '/'});
	this.route('login_failed');

});

MyApp.LoginController = Ember.ObjectController.extend({
	model: {},

	  actions: {
		  login: function(){
		  console.log(this);
		  var c = this;
			 console.log(this.get('username'));
	     var signin = this.store.createRecord('auth_user', {
			        login: this.get('username'),
			        pwd: this.get('password')
	      })

	     signin.save().then(function(user) {
	    	 location.href = 'application.html'
	    	}, function(response) {
	    	    c.transitionToRoute('login_failed');
	    	});
	      console.log('end');
	    }
	  }
});


MyApp.AuthUser = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    roles: DS.attr('string'),
    status:DS.attr('string'),
    error:DS.attr('string')
});
