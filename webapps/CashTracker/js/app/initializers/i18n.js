Ember.Application.initializer({
    name: 'i18n',

    initialize: function(container, application) {
        application.register('service:i18n', Ember.Object);
        application.inject('route', 'i18n', 'service:i18n');
    }
});
