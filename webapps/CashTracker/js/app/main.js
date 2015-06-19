'use strict';

var CT = Ember.Application.create({
    modulePrefix: 'CashTracker',
    LOG_TRANSITIONS: true,
    LOG_TRANSITIONS_INTERNAL: true,
    LOG_ACTIVE_GENERATION: true
});

// CT.ApplicationAdapter = DS.FixtureAdapter;

CT.ApplicationAdapter = DS.RESTAdapter.extend({
    pathForType: function(type) {
        console.log(type);
        switch (type) {
            case 'costCenter':
                return 'costcenters';
            case 'category':
                return 'categories';
            default:
                return type + 's';
        }
    }
});

DS.RESTAdapter.reopen({
    namespace: 'CashTracker/rest'
});

/*CT.ApplicationAdapter = DS.FirebaseAdapter.extend({
    firebase: new Firebase('https://blinding-fire-6380.firebaseio.com/')
});
*/
