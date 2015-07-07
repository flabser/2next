Ember.Application.initializer({
    name: 'session',

    initialize: function(container, application) {
        application.register('service:session', Ember.Object);
        application.inject('route', 'session', 'service:session');
    }
});
