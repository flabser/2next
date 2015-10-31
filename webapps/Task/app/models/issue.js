import DS from 'ember-data';

export default DS.Model.extend({
    status: DS.attr('string'),
    executor: DS.belongsTo('user'),
    deadline: DS.attr('date'),
    body: DS.attr('string'),
    category: DS.belongsTo('category'),
    task: DS.belongsTo('task')
});
