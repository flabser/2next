MyApp = Ember.Application.create();

//App.ApplicationAdapter = DS.FixtureAdapter;
//
var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});

MyApp.ApplicationAdapter = host.extend({
    namespace: 'Administrator/rest'
});


MyApp.Router.map(function(){
	this.route('signup');
	this.route('login', {path:'/'});
/*	 this.resource('user', function() {
		 		this.route('new');
		    });*/
	this.route('user');
	this.route('outline');
		
		
		
});


MyApp.OutlineController = Ember.ObjectController.extend({
	 actionopen: function(event) {
	        console.log("User Action");
	        this.transitionToRoute("outline");
	    }
});


MyApp.SigninController = Ember.ObjectController.extend({
	model: {},

	  actions: {
		  login: function(){ 
		  console.log(this);
		  var c = this;
			  console.log(this.get('username'));
	  		console.log(this.get('password'));
	     var signin = this.store.createRecord('signin', {	    	
			        login: this.get('username'),	    	  	
			        pwd: this.get('password')
	      })
	      
	     signin.save().then(function() {
	    	  console.log("Post saved.");
	    	  console.log(c);
	    	 // c.get('outline').send('actionopen', event);
	    	  c.transitionToRoute('outline');
	    	}, function(response) {
	    	  console.error("Post not saved!" );
	    	});
	      console.log('end');
	    }	 
	  }	
});














// export default loginController;


/* login: function() {
console.log("login");
var users = this.store.find('user',{ username: this.get('user')})
// var users = this.store.find('user');
console.log(users);
users.then(function loggedIn(){
	  console.log("login2");
	  var currentUser = users.get('firstObject');
	  this.session.set('user', currentUser);

	  var previousTransition = this.get('previousTransition');
	  if(previousTransition){
		  this.set('previousTransition',null);
		  console.log('retrying');
		  previousTransition.retry();
	  }else{
		  this.transitionToRoute('index');
	  }
}.bind(this));
}*/
MyApp.Signin = DS.Model.extend({
	login:DS.attr('string'),
	pwd:DS.attr('string'),
	status:DS.attr('string'),
	error:DS.attr('string')
});


MyApp.User = DS.Model.extend({
	login:DS.attr('string'),
	userName:DS.attr('string')
});


MyApp.OutlineRoute = Ember.Route.extend({
  
});

MyApp.SigninRoute = Ember.Route.extend({
  model: function() {
	  console.log("try to login");
	  return this.store.find('signin');
  }
});

MyApp.UserRoute = Ember.Route.extend({
	model: function(params) {
	  console.log("get users");
	  return this.store.find('user');
  }
});

MyApp.ApplicationView = Ember.View.extend({
  templateName: 'application'
})
MyApp.ContentView = Ember.View.extend({
  templateName: 'content'
})
MyApp.FooterView = Ember.View.extend({
  templateName: 'footer'
})