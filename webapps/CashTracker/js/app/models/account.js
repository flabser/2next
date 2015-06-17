CT.Account = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    currency: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    owner: DS.belongsTo('user'),
    observers: DS.belongsTo('user')
});

var _fixtures = [];

for (var ii = 1; ii < 50; ii++) {
    _fixtures.push({
        id: ii,
        type: ii,
        name: 'mk-' + ii,
        currency: 'KZT',
        openingBalance: ii,
        amountControl: ii,
        owner: 'medet',
        observers: 'medet'
    });
}

CT.Account.FIXTURES = _fixtures;
