import DS from 'ember-data';

export default DS.Model.extend({
    status: DS.attr('string'),
    priority: DS.attr('number'),
    executor: DS.belongsTo('user'),
    milestone: DS.attr('date'),
    body: DS.attr('string'),
    tags: DS.hasMany('tag')
});
