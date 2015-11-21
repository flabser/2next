import DS from 'ember-data';

export default DS.Model.extend({
    name: DS.attr('string'),

    parent: DS.belongsTo('tag'),
    children: DS.hasMany('tag', {
        inverse: 'parent'
    }),

    author: DS.belongsTo('user'),
    regDate: DS.attr('date')
});
