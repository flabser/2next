MyApp.SigninRoute = Ember.Route.extend({
  model: function() {
	  console.log("try to login");
	  return this.store.find('signin');
  }
});
