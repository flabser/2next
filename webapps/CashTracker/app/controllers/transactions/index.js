import Em from 'ember';

export default Em.Controller.extend({
    transactions: Em.computed.alias('model'),

    queryParams: ['type', 'offset', 'limit', 'order_by'],

    hasAddAction: true,

    actions: {
        composeRecord() {
            this.transitionToRoute('transactions.new');
        }
    }
});
