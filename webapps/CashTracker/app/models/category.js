import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    transactionType: DS.attr('number'),
    parentId: DS.belongsTo('category', {
        async: false
    }),
    name: DS.attr('string'),
    note: DS.attr('string'),
    color: DS.attr('number'),
    sortOrder: DS.attr('number'),

    validations: {
        name: {
            presence: true
        },
        color: {
            numericality: true,
            inclusion: {
                range: [0, 10],
                allowBlank: true
            }
        },
        sortOrder: {
            numericality: true,
            allowBlank: true
        }
    }
});
