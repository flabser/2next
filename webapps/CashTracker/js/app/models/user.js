CT.User = DS.Model.extend({
    name: DS.attr('string')
});

CT.User.FIXTURES = [{
    id: '1',
    name: 'User 1'
}, {
    id: '2',
    name: 'User 2'
}];
