import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('category');
    },

    actions: {
        add() {
            this.transitionTo('categories.new');
        }
    }
});
