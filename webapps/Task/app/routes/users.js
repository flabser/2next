import Em from 'ember';

export default Em.Route.extend({
    beforeModel: function() {
        console.log('users beforeModel, get check changes');
    },

    model: function() {
        // if has changes this.store.query('user') else peekAll
        return this.store.peekAll('user');
    },

    setupController: function(controller, model) {
        controller.set('users', model);
    },

    renderTemplate: function(controller, model) {
        var usersController = this.controllerFor('users');

        this.render('users', {
            into: 'application',
            controller: usersController
        });
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('users.invitation');
        },

        sendInvite: function(invitation) {
            invitation.save().then(() => {
                this.transitionTo('users');
            });
        },

        deleteUser: function(user) {
            user.destroyRecord().then(() => {
                this.transitionTo('users');
            }, function(resp) {
                user.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
