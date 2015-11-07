AdminApp.AppController = Ember.Controller.extend({
    actions: {
        save: function(app) {
        	console.log("save app");
            app.save();
            this.transitionTo('apps');
        }
    }
});
