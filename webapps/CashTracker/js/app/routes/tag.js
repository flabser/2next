CT.TagsTagRoute = Ember.Route.extend({
    templateName: 'tag',

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
