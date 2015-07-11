CT.CategoriesCategoryRoute = Ember.Route.extend({
    templateName: 'category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    actions: {
        save: function(category) {
            category.save();
            this.transitionTo('categories');
        }
    }
});
