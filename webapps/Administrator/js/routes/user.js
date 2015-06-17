App.UserRoute = Ember.Route.extend({
  model: function() {
	  console.log("get users");
	  return this.store.find('user');
  }
});
