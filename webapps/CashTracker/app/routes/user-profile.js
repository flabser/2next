import Ember from 'ember';

const {
    Route, inject
} = Ember;

export default Route.extend({

    session: inject.service(),

    model: function() {
        return this.get('session.user');
    },

    actions: {
        save: function() {
            var _this = this;
            var model = this.currentModel;
            model.save().then(function() {
                _this.send('goBack');
            });
        }
    }
});
