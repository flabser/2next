import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return [];
    },

    actions: {
        add() {
            this.transitionTo('transactions.new');
        }
    }
});
