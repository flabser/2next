import Em from 'ember';

export default Em.Controller.extend({
    actions: {
        transitionToNewInviteRoute: function() {
            this.transitionToRoute('settings.users.invitation');
        },

        sendInvite: function(invitation) {
            invitation.save().then(() => {
                this.transitionToRoute('users');
            });
        },

        deleteUser: function(user) {
            user.destroyRecord().then(() => {
                this.transitionToRoute('settings.users');
            }, function(resp) {
                user.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
