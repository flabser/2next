MyApp = Ember.Application.create();

//App.ApplicationAdapter = DS.FixtureAdapter;
//
var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});

var baseURL = host.extend({
    namespace: 'Administrator/rest'
});

MyApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
        switch (type) {
            case 'authUser':
                return 'session';
            default:
                return type + 's';
        }
    }
});
