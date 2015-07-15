import Ember from 'ember';

export default Ember.Route.extend({
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
