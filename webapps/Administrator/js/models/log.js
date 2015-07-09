AdminApp.Log = DS.Model.extend({
    name: DS.attr('string'),
    length: DS.attr('number'),
    lastModified: DS.attr('string')
    
});
