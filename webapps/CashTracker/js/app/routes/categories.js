CT.CategoriesRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('category');
    }
});
