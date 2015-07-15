import DS from 'ember-data';

export default DS.Model.extend({
    user: DS.belongsTo('user', {
        async: false
    }),
    accountFrom: DS.belongsTo('account', {
        async: false
    }),
    accountTo: DS.belongsTo('account', {
        async: false
    }),
    amount: DS.attr('number'),
    regDate: DS.attr('date'),
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
    repeat: DS.attr('boolean'),
    every: DS.attr('number'),
    repeatStep: DS.attr('number'),
    startDate: DS.attr('date'),
    endDate: DS.attr('date'),
    basis: DS.attr('string'),
    note: DS.attr('string'),
    includeInReports: DS.attr('boolean')
});
