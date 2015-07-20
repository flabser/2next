import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'users/user',

    model: function(params) {
        return this.store.find('user', params.user_id);
    },

    actions: {
        save: function() {
            var _this = this;
            var model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('users');
            });
        }
    }
});
