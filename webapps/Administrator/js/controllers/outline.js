MyApp.OutlineController = Ember.ObjectController.extend({
	 actionopen: function(event) {
	        console.log("User Action");
	        this.transitionToRoute("outline");
	    }
});

