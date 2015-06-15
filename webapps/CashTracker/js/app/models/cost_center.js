CT.CostCenter = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string')
});

CT.CostCenter.FIXTURES = [{
    id: 1,
    type: 1,
    name: 'ala'
}, {
    id: 2,
    type: 2,
    name: 'ast'
}];
