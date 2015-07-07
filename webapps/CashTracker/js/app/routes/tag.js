CT.TagRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('tag', params.tag_id);
    },

    actions: {
        save: function(tag) {
            tag.save();
            this.transitionTo('tags');
        }
    }
});
