import Em from 'ember';

const {
    Route, inject
} = Em;

export default Route.extend({
    session: inject.service(),

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
