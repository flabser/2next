import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    // type: DS.attr('number'),
    name: DS.attr('string'),
    currencyCode: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    owner: DS.belongsTo('user', {
        async: false
    }),
    observers: DS.hasMany('user', {
        async: false
    }),
    includeInTotals: DS.attr('boolean'),
    note: DS.attr('string'),
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
        sortOrder: {
            numericality: true
        }
    }
});
