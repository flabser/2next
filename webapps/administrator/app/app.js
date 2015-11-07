AdminApp = Ember.Application.create();

var baseURL =  DS.RESTAdapter.extend({
    namespace: 'rest'
});

AdminApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
           return type + 's';

    }
});
