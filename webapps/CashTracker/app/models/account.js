import DS from 'ember-data';

export default DS.Model.extend({
    name: DS.attr('string'),
    currencyCode: DS.attr('string'),
    openingBalance: DS.attr('number', {
        defaultValue: 0
    }),
    amountControl: DS.attr('number', {
        defaultValue: 0
    }),
    enabled: DS.attr('boolean', {
        defaultValue: true
    }),
    note: DS.attr('string'),
    owner: DS.hasMany('user', {
        async: true
    }),
    observers: DS.hasMany('user', {
        async: true
    })
});
