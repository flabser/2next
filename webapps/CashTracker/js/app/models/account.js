CT.Account = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    currency: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    owner: DS.belongsTo('user'),
    observers: DS.belongsTo('user')
});

CT.Account.FIXTURES = [{
    id: 1,
    type: 1,
    name: 'mk',
    currency: 'KZT',
    openingBalance: 0,
    amountControl: 0,
    owner: 'medet',
    observers: 'medet'
}, {
    id: 2,
    type: 2,
    name: 'flabser',
    currency: 'KZT',
    openingBalance: 0,
    amountControl: 0,
    owner: 'dzhilian',
    observers: 'medet'
}, {
    id: 3,
    type: 3,
    name: 'flabser',
    currency: 'KZT',
    openingBalance: 0,
    amountControl: 0,
    owner: 'dzhilian',
    observers: 'medet'
}, {
    id: 4,
    type: 5,
    name: 'flabser',
    currency: 'KZT',
    openingBalance: 0,
    amountControl: 0,
    owner: 'dzhilian',
    observers: 'medet'
}];
