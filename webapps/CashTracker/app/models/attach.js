import Em from 'ember';
import DS from 'ember-data';

export default DS.Model.extend({
    fieldName: DS.attr('string'),
    realFileName: DS.attr('string'),
    tempID: DS.attr('string'),
    size: DS.attr('string', {
        readOnly: true
    }),

    url: Em.computed('fieldName', 'realFileName', function() {
        return `this.get('fieldName')/this.get('realFileName')`;
    })
});
