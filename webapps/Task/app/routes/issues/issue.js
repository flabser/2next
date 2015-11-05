import Em from 'ember';
import ModelRoute from '../../mixins/routes/model';

export default Em.Route.extend(ModelRoute, {
    templateName: 'issues/issue',

    model: function(params) {
        return this.store.find('issue', params.issue_id);
    },

    setupController: function(controller, model) {
        controller.set('issue', model);
        controller.set('tags', this.store.findAll('tag'));
    }
});
