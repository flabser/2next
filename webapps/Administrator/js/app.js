var MyApp = Ember.Application.create({
    modulePrefix: 'Administrator',
    LOG_TRANSITIONS: true,
    LOG_TRANSITIONS_INTERNAL: true,
    LOG_ACTIVE_GENERATION: true
});

MyApp.ApplicationAdapter = DS.RESTAdapter.extend({
    pathForType: function(type) {
          return type + 's';       
    }
});

DS.RESTAdapter.reopen({
    namespace: 'Administrator/rest'
});

