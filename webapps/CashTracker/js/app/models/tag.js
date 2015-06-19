CT.Tag = DS.Model.extend({
    name: DS.attr('string'),
    color: DS.attr('number')
});

var _fixtures = [];

for (var ii = 1; ii < 10; ii++) {
    _fixtures.push({
        id: ii,
        name: 'tag ' + ii,
        color: 0
    });
}

CT.Tag.FIXTURES = _fixtures;
