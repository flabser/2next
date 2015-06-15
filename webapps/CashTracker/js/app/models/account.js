CT.Account = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    currency: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    owner: DS.attr('string'),
    observers: DS.attr('string')
});
