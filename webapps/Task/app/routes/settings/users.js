import Em from 'ember';
import ModelRouteMixin from 'lof-task/mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    model: function() {
        return this.store.findAll('user');
    },

    setupController: function(controller, model) {
        controller.set('users', model);
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('settings.users.invitation');
        },

        sendInvite: function(invitation) {
            invitation.save().then(() => {
                this.transitionTo('users');
            });
        },

        deleteUser: function(user) {
            user.destroyRecord().then(() => {
                this.transitionTo('settings.users');
            }, function(resp) {
                user.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
