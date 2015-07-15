import DS from 'ember-data';

export default DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    currencyCode: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    owner: DS.belongsTo('user', {
        async: false
    }),
    observers: DS.hasMany('user', {
        async: false
    }),
    includeInTotals: DS.attr('boolean'),
    note: DS.attr('string'),
    sortOrder: DS.attr('number')
});
