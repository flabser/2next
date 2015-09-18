import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.get('session.user');
    },

    setupController: function(controller, model) {
        controller.set('userProfile', model);
    }
});
