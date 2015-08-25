import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'cost-centers/cost-center',

    model: function(params) {
        return this.store.find('cost_center', params.costcenter_id);
    },

    setupController: function(controller, model) {
        controller.set('costCenter', model);
    },

    actions: {
        save: function() {
            var _this = this;
            var model = this.get('controller').get('costCenter');
            model.save().then(function() {
                _this.transitionTo('cost_centers');
            });
        }
    }
});
