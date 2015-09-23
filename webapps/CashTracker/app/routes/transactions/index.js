import Em from 'ember';
import InfinityRoute from "ember-infinity/mixins/route";

export default Em.Route.extend(InfinityRoute, {
    perPageParam: "limit", // instead of "per_page"
    pageParam: "page", // instead of "page"
    totalPagesParam: "meta.total_pages", // instead of "meta.total_pages"

    queryParams: {
        type: {
            refreshModel: true
        },
        offset: {
            refreshModel: true
        },
        limit: {
            refreshModel: true
        },
        order_by: {
            refreshModel: true
        }
    },

    model: function(params) {
        console.log(params);
        //return this.store.query('transaction', params);

        return this.infinityModel("transaction", {
            perPage: 20,
            startingPage: 1,
            type: params.type
        });
    },

    beforeModel: function(transition) {
        if (transition.targetName === 'transactions.index') {
            if (!parseInt(transition.queryParams.limit, 0)) {
                transition.queryParams.limit = 20;
            }

            if (!parseInt(transition.queryParams.offset, 0)) {
                transition.queryParams.offset = 0;
            }

            if (!transition.queryParams.order_by || transition.queryParams.order_by === 'undefined') {
                transition.queryParams.order_by = '';
            }

            if (!transition.queryParams.type || transition.queryParams.type === 'undefined') {
                transition.queryParams.type = '';
            }

            this.transitionTo('transactions', {
                queryParams: transition.queryParams
            });
        }

        this._super(transition);
    },

    deactivate: function() {
        this._super();
        this.store.unloadAll('transaction');
    },
});
