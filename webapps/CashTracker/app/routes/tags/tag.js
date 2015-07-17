import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'tag',

    model: function(params) {
        return this.store.find('tag', params.tag_id);
    },

    actions: {
        afterSave: function(model) {
            console.log('route action save', model);
            // this.transitionTo('tags');
        }
    }
});
