CT.Transaction = DS.Model.extend({
    author: DS.attr('string'),
    regDate: DS.attr('date'),
    date: DS.attr('date'),
    endDate: DS.attr('date'),
    parentCategory: DS.attr('number'),
    category: DS.attr('number'),
    account: DS.attr('number'),
    costCenter: DS.attr('number'),
    amount: DS.attr('number'),
    repeat: DS.attr('repeat'),
    every: DS.attr('every'),
    repeatStep: DS.attr('repeatStep'),
    basis: DS.attr('string'),
    comment: DS.attr('string')
});
