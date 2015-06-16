CT.Transaction = DS.Model.extend({
    author: DS.attr('string'),
    regDate: DS.belongsTo('user'),
    date: DS.attr('date'),
    endDate: DS.attr('date'),
    parentCategory: DS.attr('number'),
    category: DS.attr('number'),
    account: DS.attr('number'),
    costCenter: DS.attr('number'),
    amount: DS.attr('number'),
    repeat: DS.attr('repeat'),
    every: DS.attr('every'),
    repeatStep: DS.attr('repeatStep'),
    basis: DS.attr('string'),
    comment: DS.attr('string')
});

var _fixtures = [];

for (var ii = 1; ii < 200; ii++) {
    _fixtures.push({
        id: ii,
        author: 'mkalihan',
        regDate: '11.11.2015',
        date: '11.11.2015',
        endDate: '15.11.2015',
        parentCategory: ii,
        category: ii,
        account: ii,
        costCenter: ii,
        amount: 1000 + ii,
        repeat: ii,
        every: 0,
        repeatStep: ii,
        basis: 'test basis ' + ii,
        comment: 'test comment ' + ii
    });
}

CT.Transaction.FIXTURES = _fixtures;
