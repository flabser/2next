import Em from 'ember';

export default Em.Route.extend({
    templateName: 'cost-centers/cost-center',

    model: function(params) {
        return this.store.find('cost_center', params.costcenter_id);
    },

    deactivate: function() {
        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') == false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
    }
});
