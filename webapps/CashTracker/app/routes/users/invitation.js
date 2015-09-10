import Em from 'ember';
import ModelRoute from '../../mixins/model-route';

export default Em.Route.extend(ModelRoute, {
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
