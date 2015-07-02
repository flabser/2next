AdminApp.Router.map(function(){
	//this.route('users');
	this.route('main_window',{path: '/'});
	this.route('logs');

//	this.route('user');

//	this.route('user', {path: '/users/:user_id'});
	this.route('users', function() {this.route('new'); });
});

