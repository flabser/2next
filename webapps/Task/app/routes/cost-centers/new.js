import CostCenterRoute from './cost-center';

export default CostCenterRoute.extend({
    model: function() {
        return this.store.createRecord('costCenter');
    }
});
