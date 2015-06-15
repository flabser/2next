CT.User = DS.Model.extend({
    name: DS.attr('string'),
    email: DS.attr('string')
});

CT.User.FIXTURES = [{
    id: 'medet',
    name: 'mkalihan',
    email: ''
}, {
    id: 'dzhilian',
    name: 'dzhilian',
    email: ''
}];
