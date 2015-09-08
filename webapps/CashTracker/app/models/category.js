import DS from 'ember-data';

export default DS.Model.extend({
    transactionTypes: DS.attr(),
    parentCategory: DS.belongsTo('category', {
        async: false
    }),
    name: DS.attr('string'),
    enabled: DS.attr('boolean', {
        defaultValue: true
    }),
    note: DS.attr('string'),
    color: DS.attr('number')
});
