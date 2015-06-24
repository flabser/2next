
App.Router.map(function(){
	this.route('signup');
	this.route('login', {path:'/'});
/*	 this.resource('user', function() {
		 		this.route('new');
		    });*/
	this.route('user');
	this.route('outline');
		
		
		
});

