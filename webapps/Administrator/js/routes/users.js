App.UsersRoute = Ember.Route.extend({
  model: function() {
    //return this.store.find('user');
	  console.log("it is users route");
	  //console.log(this.store.find('user'));
	  return this.store.find('user');
	  return ['blue'];
  }
});
