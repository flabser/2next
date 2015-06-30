MyApp.UsersRoute = Ember.Route.extend({
      model: function() {
          	  console.log("router >get users");
          	  return this.store.find('user');
            }

});
