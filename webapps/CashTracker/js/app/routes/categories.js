CT.CategoriesRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('category');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.CategoriesNewRoute = Ember.Route.extend({
    templateName: 'category'
});
