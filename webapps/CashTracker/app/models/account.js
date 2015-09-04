import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    // type: DS.attr('number'),
    name: DS.attr('string'),
    currencyCode: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    enabled: DS.attr('boolean', {
        defaultValue: true
    }),
    /*includeInTotals: DS.attr('boolean', {
        defaultValue: true
    }),*/
    note: DS.attr('string'),
    owner: DS.hasMany('user', {
        async: true
    }),
    observers: DS.hasMany('user', {
        async: true
    }),

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
