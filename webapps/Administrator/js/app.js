AdminApp = Ember.Application.create();
/*

var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});
*/
//AdminApp.ApplicationAdapter = DS.FixtureAdapter;

var baseURL =  DS.RESTAdapter.extend({
    namespace: 'Administrator/rest'
});

AdminApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
        	console.log("type=" + type);
           return type + 's';

    }
});
