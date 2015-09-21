import Em from 'ember';
import InfinityRoute from "ember-infinity/mixins/route";

export default Em.Route.extend(InfinityRoute, {
    perPageParam: "limit", // instead of "per_page"
    pageParam: "page", // instead of "page"
    totalPagesParam: "meta.total", // instead of "meta.total_pages"

    model: function() {
        // return this.store.findAll('cost-center');
        return this.infinityModel("cost-center", {
            perPage: 20,
            startingPage: 1
        });
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('cost_centers.new');
        },

        saveCostCenter: function(costCenter) {
            costCenter.save().then(() => {
                this.transitionTo('cost_centers');
            });
        },

        deleteRecord: function(costCenter) {
            costCenter.destroyRecord().then(() => {
                this.transitionTo('cost_centers');
            }, function(resp) {
                costCenter.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
