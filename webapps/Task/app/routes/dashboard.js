import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return [];
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('transactions.new');
        }
    }
});
