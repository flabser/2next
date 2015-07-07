CT.TagsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('tag');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.TagsNewRoute = Ember.Route.extend({
    templateName: 'tag',

    actions: {
        create: function() {
            this.transitionTo('tags.new');
        },
        save: function() {
            var newTag = this.store.createRecord('tag', {
                name: this.get('name'),
                color: this.get('color')
            });
            newTag.save();
        },
        cancel: function() {
            this.transitionTo('tags');
        }
    }
});
