import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('cost-center');
    },

    actions: {
        add() {
            this.transitionTo('cost_centers.new');
        }
    }
});
