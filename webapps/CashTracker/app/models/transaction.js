import DS from 'ember-data';

export default DS.Model.extend({
    account: DS.belongsTo('account', {
        async: false
    }),
    transferAccount: DS.belongsTo('account', {
        async: false
    }),
    amount: DS.attr('number'),
    date: DS.attr('string'),
    category: DS.belongsTo('category', {
        async: false
    }),
    costCenter: DS.belongsTo('costCenter', {
        async: false
    }),
    tags: DS.hasMany('tag'),
    transactionState: DS.attr('string'),
    transactionType: DS.attr('string', {
        defaultValue: 'E'
    }),
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
