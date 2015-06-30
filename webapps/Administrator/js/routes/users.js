MyApp.UsersRoute = Ember.Route.extend({
/*
	 renderTemplate: function() {
        var controller = this.controllerFor('users');

        // Render the `favoritePost` template into
        // the outlet `posts`, and use the `favoritePost`
        // controller.
        this.render('users_list', {
          outlet: 'viewOutlet',
          controller: controller
        });
      }
*/

      model: function(params) {
          	  console.log("router >get users");
          	  return this.store.find('user');
            }

});
