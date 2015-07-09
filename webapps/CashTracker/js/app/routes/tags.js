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
        save: function(tag) {
            var controller = this.controller;
            var newTag = this.store.createRecord('tag', {
                name: controller.get('name'),
                color: controller.get('color')
            });
            newTag.save();
        },
        cancel: function() {
            this.transitionTo('tags');
        }
    }
});
