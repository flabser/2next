import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('user');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('users.invitation');
        },

        deleteRecord: function(user) {
            var _this = this;
            user.destroyRecord().then(function() {
                _this.transitionTo('users');
            }, function(resp) {
                user.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
