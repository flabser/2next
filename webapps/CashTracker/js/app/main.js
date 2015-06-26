'use strict';

var CT = Ember.Application.create({
    modulePrefix: 'CT',
    LOG_TRANSITIONS: true,
    LOG_TRANSITIONS_INTERNAL: true,
    LOG_ACTIVE_GENERATION: true
});

CT.ApplicationAdapter = DS.RESTAdapter.extend({
    pathForType: function(type) {
        switch (type) {
            case 'authUser':
                return 'session';
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
