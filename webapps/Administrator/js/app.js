AdminApp = Ember.Application.create();

var baseURL =  DS.RESTAdapter.extend({
    namespace: 'Administrator/rest'
});

AdminApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
        	console.log("type=" + type);
           return type + 's';

    }
});
