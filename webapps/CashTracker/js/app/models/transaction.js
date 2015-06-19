CT.Transaction = DS.Model.extend({
    user: DS.belongsTo('user'),
    accountFrom: DS.belongsTo('account'),
    accountTo: DS.belongsTo('account'),
    amount: DS.attr('number'),
    regDate: DS.attr('date'),
    category: DS.belongsTo('category'),
    costCenter: DS.belongsTo('costCenter'),
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

for (var ii = 1; ii < 20; ii++) {
    _fixtures.push({
        id: ii,
        user: 'mkalihan',
        accountFrom: 1,
        accountTo: 2,
        amount: 1000,
        regDate: '11.11.2015',
        category: 1,
        costCenter: 1,
        tags: [],
        transactionState: 1,
        transactionType: 1,
        exchangeRate: 1.1,
        repeat: false,
        every: 0,
        repeatStep: 0,
        startDate: null,
        endDate: null,
        basis: '',
        note: '',
        includeInReports: true
    });
}

CT.Transaction.FIXTURES = _fixtures;
