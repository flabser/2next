CT.TagRoute = Ember.Route.extend({
    templateName: 'tag',

    model: function(params) {
        return this.store.find('tag', params.tag_id);
    }
});
