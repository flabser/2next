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
            var _this = this;
            this.session.saveUserProfile().then(function() {
                _this.sendAction('goBack');
            });
        }
    }
});
