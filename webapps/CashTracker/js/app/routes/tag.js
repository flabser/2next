CT.TagRoute = Ember.Route.extend({
    templateName: 'tag',

    model: function(params) {
        return this.store.find('tag', params.tag_id);
    },

    actions: {
        save: function(tag) {
            tag.save().then(function() {
                this.transitionTo('tags');
            });
        }
    }
});

CT.TagsNewRoute = Ember.Route.extend({
    templateName: 'tag'
});
