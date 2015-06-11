CT.CategoryRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('category', params.category_id);
    }
});
