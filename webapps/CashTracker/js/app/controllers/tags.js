CT.TagsController = Ember.ArrayController.extend({
    actions: {
        selectAll: function() {}
    }
});

CT.TagsNewController = Ember.ArrayController.extend({
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
