import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.find('budget', 1);
    },

    actions: {
        add() {
            this.transitionTo('transactions.new');
        }
    }
});
