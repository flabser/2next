import Ember from 'ember';

export default Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('category');
    },

    actions: {
        selectAll: function() {}
    }
});
