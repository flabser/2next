import Em from 'ember';
import ModelRoute from '../../mixins/routes/model';

export default Em.Route.extend(ModelRoute, {
    templateName: 'issues/index',

    model: function(params) {
        return this.store.find('issue', params.task_id);
    },

    renderTemplate: function(controller, model) {
        this.render(this.get('templateName'), {
            into: 'tasks',
            outlet: 'task',
            model: model
        });
    }
});
