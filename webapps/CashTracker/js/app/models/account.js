CT.Account = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    currencyCode: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    owner: DS.belongsTo('user'),
    observers: DS.hasMany('user'),
    includeInTotals: DS.attr('boolean'),
    note: DS.attr('string'),
    sortOrder: DS.attr('number')
});

var _fixtures = [];

for (var ii = 1; ii < 10; ii++) {
    _fixtures.push({
        id: ii,
        type: ii,
        name: 'account-' + ii,
        currencyCode: 'KZT',
        openingBalance: 1000 + ii,
        amountControl: 2000 + ii,
        owner: 'mkalihan',
        observers: ['mkalihan'],
        includeInTotals: true,
        note: 'note-' + ii,
        sortOrder: ii
    });
}

CT.Account.FIXTURES = _fixtures;
