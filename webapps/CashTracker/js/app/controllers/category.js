CT.CategoryController = Ember.ObjectController.extend({
    actions: {
        save: function(category) {
            category.save();
            this.transitionTo('categories');
        }
    }
});
