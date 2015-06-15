App.UsersListRoute = Ember.Route.extend({
  model: function() {
	  //return ['blue','black'];
	  return this.store.find('user');
  }
});
