import Em from 'ember';

export default Em.Route.extend({
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
        return this.store.query('transaction', params);
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

    actions: {
        paginatePrev: function() {
            this.get('controller').set('offset', (+this.get('controller.offset') - 20));
            this.transitionTo('transactions', {
                queryParams: this.get('controller').queryParams
            });
        },
        paginateNext: function() {
            this.get('controller').set('offset', (+this.get('controller.offset') + 20));
            this.transitionTo('transactions', {
                queryParams: this.get('controller').queryParams
            });
        }
    }
});
