CT.Category = DS.Model.extend({
    transactionType: DS.attr('number'),
    parentId: DS.belongsTo('category'),
    name: DS.attr('string'),
    note: DS.attr('string'),
    color: DS.attr('number'),
    sortOrder: DS.attr('number')
});

var _fixtures = [];

for (var ii = 1; ii < 0; ii++) {
    _fixtures.push({
        id: ii,
        type: ii,
        name: 'car ' + ii,
        comment: 'car expense ' + ii
    });
}

CT.Category.FIXTURES = _fixtures;
