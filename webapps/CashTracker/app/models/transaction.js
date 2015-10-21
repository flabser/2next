import DS from 'ember-data';

export default DS.Model.extend({
    account: DS.belongsTo('account'),
    transferAccount: DS.belongsTo('account'),
    category: DS.belongsTo('category'),
    costCenter: DS.belongsTo('costCenter'),
    tags: DS.hasMany('tag'),
    amount: DS.attr('number'),
    date: DS.attr('date', {
        defaultValue: () => new Date()
    }),
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
