CT.CostCenter = DS.Model.extend({
    name: DS.attr('string')
});

var _fixtures = [];

for (var ii = 1; ii < 10; ii++) {
    _fixtures.push({
        id: ii,
        name: 'cost_center-' + ii
    });
}

CT.CostCenter.FIXTURES = _fixtures;
