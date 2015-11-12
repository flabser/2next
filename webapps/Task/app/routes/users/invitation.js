import Em from 'ember';
import ModelRouteMixin from 'lof-task/mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    model: function() {
        return this.store.createRecord('invitation');
    },

    setupController: function(controller, model) {
        controller.set('invitation', model);
    },

    renderTemplate: function(controller, model) {
        var usersController = this.controllerFor('users');

        this.render('users.invitation', {
            into: 'application',
            controller: usersController
        });
    }
});
