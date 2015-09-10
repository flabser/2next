import Em from 'ember';

export default Em.Route.extend({
    session: Em.inject.service(),

    model: function() {
        return this.get('session.user');
    },

    setupController: function(controller, model) {
        controller.set('userProfile', model);
    },

    actions: {
        saveUserProfile: function() {
            this.session.saveUserProfile().then(() => {
                this.sendAction('goBack');
            });
        }
    }
});
