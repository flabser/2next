import Em from 'ember';

export default Em.Route.extend({
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
