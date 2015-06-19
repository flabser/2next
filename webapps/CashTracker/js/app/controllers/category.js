CT.CategoryController = Ember.ObjectController.extend({
    actions: {
        save: function(category) {
        	console.log(category);
            category.save().then(function() {
                this.transitionTo('categories');
            });
        }
    }
});
