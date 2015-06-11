CT.UserProfile = DS.Model.extend({
    name: DS.attr('string')
});

CT.UserProfile.FIXTURES = [{
    id: '1',
    name: 'hello'
}, {
    id: '2',
    name: 'hi'
}];
