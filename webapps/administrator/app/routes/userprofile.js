
AdminApp.UserProfileRoute = Ember.Route.extend({

    model: function() {
    	var u =  this.store.get('session.user');
    	console.log(u);
        return this.get('session.user');
    },

   
    setupController: function(controller, model) {
        controller.set('userProfile', model);
    }
    
    
});
