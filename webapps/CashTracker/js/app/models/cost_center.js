CT.CostCenter = DS.Model.extend({
    name: DS.attr('string')
});

var _fixtures = [];

for (var ii = 1; ii < 30; ii++) {
    _fixtures.push({
        id: ii,
        name: 'cc ' + ii
    });
}

CT.CostCenter.FIXTURES = _fixtures;
