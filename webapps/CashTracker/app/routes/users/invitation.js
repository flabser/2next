import Em from 'ember';
import ModelRoute from '../../mixins/model-route';

export default Em.Route.extend(ModelRoute, {
    model: function() {
        return this.store.createRecord('invitation');
    },

    roles: function() {
        return this.store.findAll('role');
    }.property(),

    setupController: function(controller, model) {
        controller.set('invitation', model);
        controller.set('roles', this.get('roles'));
    },

    actions: {
        sendInvite: function(invitation) {
            invitation.save().then(() => {
                this.transitionTo('users');
            });
        }
    }
});
