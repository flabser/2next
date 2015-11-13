import DS from 'ember-data';

export default DS.Model.extend({
    status: DS.attr('string', {
        defaultValue: 'DRAFT'
    }),
    priority: DS.attr('number', {
        defaultValue: 4
    }),
    assignee: DS.belongsTo('user'),
    milestone: DS.attr('date'),
    body: DS.attr('string'),
    tags: DS.hasMany('tag')
});
