import DS from 'ember-data';

export default DS.Model.extend({
    name: DS.attr('string'),
    date: DS.attr('date', {
        readOnly: true
    }),
    owner: DS.belongsTo('user', {
        async: false,
        readOnly: true
    }),
    status: DS.attr('number', {
        readOnly: true
    })
});
