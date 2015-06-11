'use strict';

var CT = Ember.Application.create({
    LOG_TRANSITIONS: true,
    LOG_TRANSITIONS_INTERNAL: true,
    LOG_ACTIVE_GENERATION: true
});

CT.ApplicationAdapter = DS.FixtureAdapter;
