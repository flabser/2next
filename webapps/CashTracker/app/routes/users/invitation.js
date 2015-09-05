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
            var _this = this;
            invitation.save().then(function() {
                _this.transitionTo('users');
            });
        }
    }
});
