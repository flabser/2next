CT.Transaction = DS.Model.extend({
    name: DS.attr('string')
});

CT.Transaction.FIXTURES = [{
    id: '1',
    name: 'Transaction 1'
}, {
    id: '2',
    name: 'Transaction 2'
}];
