import DS from 'ember-data';

export default DS.Model.extend({
    login: DS.attr('string'),
    email: DS.attr('string'),
    userName: DS.attr('string')
});
