import Em from 'ember';
import InfinityRoute from "ember-infinity/mixins/route";

export default Em.Route.extend(InfinityRoute, {
    perPageParam: "limit", // instead of "per_page"
    pageParam: "page", // instead of "page"
    totalPagesParam: "meta.total_pages", // instead of "meta.total_pages"

    queryParams: {
        type: {
            refreshModel: true
        }
    },

    model: function(params) {
        //return this.store.query('transaction', params);

        return this.infinityModel("transaction", {
            perPage: 20,
            startingPage: 1,
            type: params.type
        });
    },

    beforeModel: function(transition) {
        if (transition.targetName === 'transactions.index') {
            if (!transition.queryParams.type || transition.queryParams.type === 'undefined') {
                transition.queryParams.type = '';
            }

            this.transitionTo('transactions', {
                queryParams: transition.queryParams
            });
        }

        this._super(transition);
    }
});
