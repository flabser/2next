CT.User = DS.Model.extend({
    name: DS.attr('string'),
    email: DS.attr('string'),
    role: DS.attr('string')
});

var _fixtures = [];

for (var ii = 1; ii < 10; ii++) {
    _fixtures.push({
        id: ii,
        name: 'mkalihan',
        email: '',
        role: ''
    });
}

CT.User.FIXTURES = _fixtures;
