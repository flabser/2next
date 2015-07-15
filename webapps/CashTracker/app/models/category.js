import DS from 'ember-data';

export default DS.Model.extend({
    transactionType: DS.attr('number'),
    parentId: DS.belongsTo('category', {
        async: false
    }),
    name: DS.attr('string'),
    note: DS.attr('string'),
    color: DS.attr('number'),
    sortOrder: DS.attr('number')
});
