import DS from 'ember-data';

export default DS.Model.extend({
    executor: DS.belongsTo('user'),
    deadline: DS.attr('date'),
    note: DS.attr('string'),
    task: belongsTo('task')
});
