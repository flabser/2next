import Em from 'ember';
import ModelRouteMixin from 'lof-task/mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    model: function() {
        return this.store.findAll('user');
    },

    setupController: function(controller, model) {
        controller.set('users', model);
    }
});
