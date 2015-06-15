CT.Category = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    comment: DS.attr('string')
});

CT.Category.FIXTURES = [{
    id: 1,
    type: 1,
    name: 'car',
    comment: 'car expense'
}, {
    id: 2,
    type: 2,
    name: 'food',
    comment: 'nam nam'
}];
