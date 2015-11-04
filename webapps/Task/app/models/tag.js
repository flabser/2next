import DS from 'ember-data';

export default DS.Model.extend({
    parent: DS.belongsTo('tag'),
    children: DS.hasMany('tag', {
        inverse: 'parent'
    }),
    name: DS.attr('string')
});
