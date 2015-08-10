import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    transactionType: DS.attr('number'),
    parentCategory: DS.belongsTo('category', {
        async: false
    }),
    name: DS.attr('string'),
    enabled: DS.attr('boolean'),
    note: DS.attr('string'),
    color: DS.attr('number'),
    sortOrder: DS.attr('number'),

    validations: {
        name: {
            presence: true
        },
        note: {
            presence: true
        },
        color: {
            numericality: true,
            inclusion: {
                range: [0, 10],
                allowBlank: true
            }
        }
    }
});
