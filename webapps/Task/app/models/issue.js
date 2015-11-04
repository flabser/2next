import DS from 'ember-data';

export default DS.Model.extend({
    status: DS.attr('string'),
    executor: DS.belongsTo('user'),
    milestone: DS.attr('date'),
    body: DS.attr('string'),
    tag: DS.belongsTo('tag')
});
