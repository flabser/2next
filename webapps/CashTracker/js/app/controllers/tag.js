CT.TagController = Ember.ObjectController.extend({
    actions: {
        save: function(tag) {
            tag.save();
            this.transitionTo('tags');
        }
    }
});
