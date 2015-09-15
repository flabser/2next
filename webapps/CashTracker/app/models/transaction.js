import DS from 'ember-data';

export default DS.Model.extend({
    accountFrom: DS.belongsTo('account', {
        async: false
    }),
    accountTo: DS.belongsTo('account', {
        async: false
    }),
    amount: DS.attr('number'),
    date: DS.attr('date'),
    category: DS.belongsTo('category', {
        async: false
    }),
    costCenter: DS.belongsTo('costCenter', {
        async: false
    }),
    tags: DS.hasMany('tag'),
    transactionState: DS.attr('number'),
    transactionType: DS.attr('number'),
    exchangeRate: DS.attr('number'),
    repeat: DS.attr('boolean', {
        defaultValue: false
    }),
    every: DS.attr('number'),
    repeatStep: DS.attr('number'),
    startDate: DS.attr('date'),
    endDate: DS.attr('date'),
    note: DS.attr('string'),
    includeInReports: DS.attr('boolean', {
        defaultValue: true
    }),
    attachments: DS.hasMany('attach')
});
