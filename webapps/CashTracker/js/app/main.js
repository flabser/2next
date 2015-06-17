'use strict';

var CT = Ember.Application.create({
    LOG_TRANSITIONS: true,
    LOG_TRANSITIONS_INTERNAL: true,
    LOG_ACTIVE_GENERATION: true
});

CT.ApplicationAdapter = DS.FixtureAdapter;

/*CT.ApplicationAdapter = DS.RestAdapter;

DS.RESTAdapter.reopen({
    namespace: 'CashTracker/RestProvider'
});*/

/*CT.ApplicationAdapter = DS.FirebaseAdapter.extend({
    firebase: new Firebase('https://blinding-fire-6380.firebaseio.com/')
});
*/
