import Ember from 'ember';

export default Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('tag');
    },

    actions: {
        selectAll: function() {},
        openNewForm: function() {
            this.transitionTo('tags.new');
        }
    }
});
