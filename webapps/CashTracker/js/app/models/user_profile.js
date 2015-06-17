CT.UserProfile = DS.Model.extend({
    name: DS.attr('string'),
    roles: DS.attr('string')
});

CT.UserProfile.FIXTURES = [{
    id: 'mkalihan',
    name: 'mkalihan',
    roles: 'transactions'
}];
