CT.Category = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    comment: DS.attr('string')
});

var _fixtures = [];

for (var ii = 1; ii < 40; ii++) {
    _fixtures.push({
        id: ii,
        type: ii,
        name: 'car ' + ii,
        comment: 'car expense ' + ii
    });
}

CT.Category.FIXTURES = _fixtures;
