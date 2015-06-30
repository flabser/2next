MyApp.UsersController = Ember.ObjectController.extend({
    	model: function(params) {
    	  console.log("get users");
    	  return this.store.find('user');
      }


});
