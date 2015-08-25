import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.createRecord('invitation');
    },

    setupController: function(controller, model) {
        controller.set('invitation', model);
    },

    actions: {
        sendInvite: function() {
            var _this = this;
            var model = this.get('controller').get('invitation');
            model.save().then(function() {
                _this.transitionTo('users');
            });
        }
    }
});
