import DS from 'ember-data';

export default DS.Model.extend({
    comment: DS.attr('string'),

    author: DS.belongsTo('user'),
    regDate: DS.attr('date')
});
