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