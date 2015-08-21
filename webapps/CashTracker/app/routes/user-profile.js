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
    },

    actions: {
        save: function() {
            this.session.saveUserProfile();

            /*var _this = this;
            var model = this.get('user');
            model.save().then(function() {
                _this.send('goBack');
            });*/
        }
    }
});
