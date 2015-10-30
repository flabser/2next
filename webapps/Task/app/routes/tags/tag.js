import Em from 'ember';
import ModelRoute from '../../mixins/routes/model';

export default Em.Route.extend(ModelRoute, {
    templateName: 'tags/tag',

    model: function(params) {
        return this.store.find('tag', params.tag_id);
    },

    setupController: function(controller, model) {
        controller.set('tag', model);
    }
});
