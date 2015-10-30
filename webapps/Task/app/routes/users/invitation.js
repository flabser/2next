import Em from 'ember';
import ModelRoute from '../../mixins/routes/model';

export default Em.Route.extend(ModelRoute, {
    model: function() {
        return this.store.createRecord('invitation');
    },

    setupController: function(controller, model) {
        controller.set('invitation', model);
        controller.set('roles', this.store.findAll('role'));
    },

    actions: {
        sendInvite: function(invitation) {
            invitation.save().then(() => {
                this.transitionTo('users');
            });
        }
    }
});
