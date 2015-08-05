import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('account');
    },

    actions: {
        add() {
            this.transitionTo('accounts.new');
        }
    }
});
