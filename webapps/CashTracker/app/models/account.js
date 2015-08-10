import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    // type: DS.attr('number'),
    name: DS.attr('string'),
    currencyCode: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    enabled: DS.attr('boolean'),
    includeInTotals: DS.attr('boolean'),
    note: DS.attr('string'),
    writers: DS.belongsTo('user', {
        async: false
    }),
    readers: DS.hasMany('user', {
        async: false
    }),
    sortOrder: DS.attr('number'),

    validations: {
        name: {
            presence: true
        },
        currencyCode: {
            presence: true
        },
        openingBalance: {
            numericality: true
        },
        amountControl: {
            numericality: true
        }
    }
});
