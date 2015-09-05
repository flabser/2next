import DS from 'ember-data';

export default DS.Model.extend({
    accountFrom: DS.belongsTo('account', {
        async: true
    }),
    accountTo: DS.belongsTo('account', {
        async: true
    }),
    amount: DS.attr('number'),
    date: DS.attr('date'),
    category: DS.belongsTo('category', {
        async: true
    }),
    costCenter: DS.belongsTo('costCenter', {
        async: true
    }),
    tags: DS.hasMany('tag'),
    transactionState: DS.attr('number'),
    transactionType: DS.attr('number'),
    exchangeRate: DS.attr('number'),
    repeat: DS.attr('boolean'),
    every: DS.attr('number'),
    repeatStep: DS.attr('number'),
    startDate: DS.attr('date'),
    endDate: DS.attr('date'),
    note: DS.attr('string'),
    includeInReports: DS.attr('boolean', {
        defaultValue: true
    }),

    validations: {
        date: {
            presence: true
        },
        amount: {
            presence: true,
            numericality: true
        },
        transactionType: {
            presence: true
        }
    }
});
