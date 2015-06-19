CT.Category = DS.Model.extend({
    transactionType: DS.attr('number'),
    parentId: DS.belongsTo('category'),
    name: DS.attr('string'),
    note: DS.attr('string'),
    color: DS.attr('number'),
    sortOrder: DS.attr('number')
});

var _fixtures = [];

for (var ii = 1; ii < 10; ii++) {
    _fixtures.push({
        id: ii,
        transactionType: 1,
        parentId: ii - 1,
        name: 'category-' + ii,
        note: 'note-' + ii,
        color: ii,
        sortOrder: ii
    });
}

CT.Category.FIXTURES = _fixtures;
