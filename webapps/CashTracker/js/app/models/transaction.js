CT.Transaction = DS.Model.extend({
    user: DS.belongsTo('user'),
    accountFrom: DS.belongsTo('account'),
    accountTo: DS.belongsTo('account'),
    amount: DS.attr('number'),
    regDate: DS.attr('date'),
    category: DS.belongsTo('category'),
    costCenter: DS.belongsTo('costсenter'),
    tags: DS.hasMany('tag'),
    transactionState: DS.attr('number'),
    transactionType: DS.attr('number'),
    exchangeRate: DS.attr('number'),
    repeat: DS.attr('repeat'),
    every: DS.attr('every'),
    repeatStep: DS.attr('repeatStep'),
    startDate: DS.attr('date'),
    endDate: DS.attr('date'),
    basis: DS.attr('string'),
    note: DS.attr('string'),
    includeInReports: DS.attr('boolean')
});

var _fixtures = [];

for (var ii = 1; ii < 0; ii++) {
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
