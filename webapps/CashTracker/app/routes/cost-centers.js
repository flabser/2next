import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('cost-center');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('cost_centers.new');
        },

        deleteRecord: function(costCenter) {
            var _this = this;
            costCenter.destroyRecord().then(function() {
                _this.transitionTo('cost_centers');
            }, function(resp) {
                costCenter.rollback();
                alert(resp.errors.message);
            });
        }
    }
});
