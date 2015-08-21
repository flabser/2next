import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return [];
    },

    actions: {
        composeRecord() {
            this.transitionTo('transactions.new');
        }
    }
});
