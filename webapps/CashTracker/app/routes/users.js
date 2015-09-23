import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('user');
    },

    deactivate: function() {
        this._super();
        this.store.unloadAll('user');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('users.invitation');
        },

        deleteRecord: function(user) {
            user.destroyRecord().then(() => {
                this.transitionTo('users');
            }, function(resp) {
                user.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
