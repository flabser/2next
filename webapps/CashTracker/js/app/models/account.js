CT.Account = DS.Model.extend({
    name: DS.attr('string')
});

CT.Account.FIXTURES = [{
    id: '1',
    name: 'account 1'
}, {
    id: '2',
    name: 'account 2'
}];
