CT.TagsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('tag');
    },

    actions: {
        selectAll: function() {},
        openNewForm: function() {
            this.transitionTo('tags.new');
        }
    }
});

CT.TagsNewRoute = Ember.Route.extend({
    templateName: 'tag',

    actions: {
        save: function(tag) {
            var newTag = this.store.createRecord('tag', {
                name: this.controller.get('name'),
                color: this.controller.get('color')
            });
            newTag.save();
        },
        cancel: function() {
            this.transitionTo('tags');
        }
    }
});
