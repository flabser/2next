import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'user',

    model: function(params) {
        return this.store.find('user', params.user_id);
    },

    actions: {
        save: function(model) {
            model.save().then(function() {
                model.transitionTo('users');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
