CT.User = DS.Model.extend({
    name: DS.attr('string'),
    email: DS.attr('string')
});

CT.User.FIXTURES = [{
    id: '1',
    name: 'mkalihan',
    email: ''
}, {
    id: '2',
    name: 'dzhilian',
    email: ''
}];
