import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('user');
    },

    actions: {
        add() {
            this.transitionTo('users.new');
        }
    }
});
