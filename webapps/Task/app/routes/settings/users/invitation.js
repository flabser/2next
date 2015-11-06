import Em from 'ember';
import ModelRouteMixin from 'lof-task/mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    model: function() {
        return this.store.createRecord('invitation');
    },

    setupController: function(controller, model) {
        controller.set('invitation', model);
    },

    actions: {
        sendInvite: function(invitation) {
            invitation.save().then(() => {
                this.transitionTo('users');
            });
        }
    }
});
