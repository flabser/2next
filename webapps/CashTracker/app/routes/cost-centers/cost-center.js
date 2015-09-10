import Em from 'ember';
import ModelRoute from '../../mixins/model-route';

export default Em.Route.extend(ModelRoute, {
    templateName: 'cost-centers/cost-center',

    model: function(params) {
        return this.store.find('cost_center', params.costcenter_id);
    },

    setupController: function(controller, model) {
        controller.set('costCenter', model);
    }
});
