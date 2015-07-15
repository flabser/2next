import DS from 'ember-data';

export default DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    email: DS.attr('string'),
    role: DS.attr('string')
});
