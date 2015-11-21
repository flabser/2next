import DS from 'ember-data';

export default DS.Model.extend({
    status: DS.attr('string', {
        defaultValue: () => 'DRAFT'
    }),
    priority: DS.attr('number', {
        defaultValue: () => 4
    }),
    assignee: DS.belongsTo('user'),
    startDate: DS.attr('date', {
        defaultValue: () => new Date()
    }),
    dueDate: DS.attr('date', {
        defaultValue: () => new Date()
    }),
    body: DS.attr('string'),
    tags: DS.hasMany('tag'),
    comments: DS.hasMany('comment'),
    attachments: DS.hasMany('attachment'),

    parent: DS.belongsTo('issue'),
    children: DS.hasMany('issue', {
        inverse: 'parent'
    }),

    author: DS.belongsTo('user'),
    regDate: DS.attr('date')
});
