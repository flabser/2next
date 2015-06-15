/*var Router = Ember.Router.extend({
	//rootURL: ENV.rootURL,
	location:'auto'
});*/


App.Router.map(function(){
	/*this.route('new-question', {path:'question/new'});
	this.resourse('questions', function(){
		this.resourse('question', {path:'question_id'}, function(){
			this.resource('answer',{path:'answers/answer_id'});
			this.route('new-answer',{path:'answers/new'});
		})
		
	})
	*/
	this.route('signup');
	this.route('login', {path:'/'});
	this.route('logout');
	this.resource('user',{path:'users/:users_id'}, function(){
	});
	//this.route('users');
	this.route('users_list');
	this.resource('lists');
		
		
		
});

//export default Router;
