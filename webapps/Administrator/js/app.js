App = Ember.Application.create();

var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});

/*App.UserAdapter = host.extend({
    namespace: 'Administrator/rest/auth'
});
*/

App.ApplicationAdapter = host.extend({
    namespace: 'Administrator/rest/admin'
});
