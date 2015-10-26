import DS from 'ember-data';

export default DS.Model.extend({
    transactionType: DS.attr('string', {
        defaultValue: 'E'
    }),
    parent: DS.belongsTo('category'),
    children: DS.hasMany('category', {
        inverse: 'parent'
    }),
    name: DS.attr('string'),
    enabled: DS.attr('boolean', {
        defaultValue: true
    }),
    note: DS.attr('string'),
    color: DS.attr('string')
});
