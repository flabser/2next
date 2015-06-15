App.LoginController = Ember.ObjectController.extend({
	/*login:null,
	pwd:null,
	previousTransition:null,*/
	content: {},

	  actions: {
		  login: function() {
	
	  		console.log(this.get('username'));
	  		console.log(this.get('password'));
	     var user = this.store.createRecord('user', {
	    	
			        login: this.get('username'),
	    	  	
			        pwd: this.get('password')
	      });

	     // this.set('listName', '');

	      user.save();
	    }
			
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
	  }	 
});

// export default loginController;