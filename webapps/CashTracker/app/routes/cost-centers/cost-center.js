import Em from 'ember';
import ModelRouteMixin from '../../mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    templateName: 'cost-centers/cost-center',

    model: function(params) {
        return this.store.find('cost_center', params.costcenter_id);
    },

    setupController: function(controller, model) {
        controller.set('costCenter', model);
    }
});
