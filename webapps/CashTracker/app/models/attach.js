import DS from 'ember-data';

export default DS.Model.extend({
    fieldName: DS.attr('string', {
        defaultValue: 'attach'
    }),
    realFileName: DS.attr('string'),
    tempID: DS.attr('string')
});
