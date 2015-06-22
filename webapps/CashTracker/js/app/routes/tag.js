CT.TagRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('tag', params.tag_id);
    }
});
