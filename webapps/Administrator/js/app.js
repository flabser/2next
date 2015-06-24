App = Ember.Application.create();

//App.ApplicationAdapter = DS.FixtureAdapter;
//
var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});

App.ApplicationAdapter = host.extend({
    namespace: 'Administrator/rest'
});
