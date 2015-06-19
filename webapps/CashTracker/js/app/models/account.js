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

for (var ii = 1; ii < 0; ii++) {
    _fixtures.push({
        id: ii,
        type: ii,
        name: 'mk-' + ii,
        currency: 'KZT',
        openingBalance: ii,
        amountControl: ii,
        owner: 'medet',
        observers: ['medet']
    });
}

CT.Account.FIXTURES = _fixtures;
