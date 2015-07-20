import CostCenterRoute from './cost-center';

export default CostCenterRoute.extend({
    model: function(params) {
        return this.store.createRecord('costCenter');
    },

    deactivate: function() {
        var model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    }
});
