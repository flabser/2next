AdminApp.Router.map(function(){
	this.resource('users', {
	    path: '/'
	  });
	this.route('logs');
	this.route('app', {path: '/app/:app_id'});
	this.route('apps');
	this.route('user', {path: '/users/:user_id'});
	this.route('users');
	this.route('newUser', {path: '/users/new'});
});

