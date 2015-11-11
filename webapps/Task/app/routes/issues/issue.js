import Em from 'ember';
import ModelRouteMixin from 'lof-task/mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    templateName: 'issues/issue',

    model: function(params) {
        return this.store.find('issue', params.issue_id);
    },

    setupController: function(controller, model) {
        controller.set('issue', model);
        controller.set('users', this.store.findAll('user'));
        controller.set('tags', this.store.findAll('tag'));
    }
});
