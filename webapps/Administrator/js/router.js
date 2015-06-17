
App.Router.map(function(){
	this.route('signup');
	this.route('signin', {path:'/'});
/*	 this.resource('user', function() {
		 		this.route('new');
		    });*/
	this.route('user');
	this.route('outline');
		
		
		
});

