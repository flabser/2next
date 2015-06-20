CT.CategoriesRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('category');
    }
});

CT.CategoriesNewRoute = Ember.Route.extend({
    templateName: 'category'
});
