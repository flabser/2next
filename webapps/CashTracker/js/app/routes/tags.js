CT.TagsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('tag');
    }
});

CT.TagsNewRoute = Ember.Route.extend({
    templateName: 'tag'
});
