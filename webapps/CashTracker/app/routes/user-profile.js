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
        this._super(controller, model);

        controller.set('user', model);
        controller.set('isNotValid', this.get('isNotValid'));
    },

    isNotValid: function() {
        var u = this.get('session.user');
        return false; // !u.pwd || u.pwd !== u.pwd_repeat;
    }.property(),

    actions: {
        save: function() {
            var _this = this;
            this.session.saveUserProfile().then(function() {
                _this.sendAction('goBack');
            });
        }
    }
});
