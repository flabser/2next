CT.CostCenter = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string')
});

var _fixtures = [];

for (var ii = 1; ii < 30; ii++) {
    _fixtures.push({
        id: ii,
        type: ii,
        name: 'ala ' + ii
    });
}

CT.CostCenter.FIXTURES = _fixtures;
