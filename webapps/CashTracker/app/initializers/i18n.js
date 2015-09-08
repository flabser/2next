export default {
    name: 'i18n',
    after: 'ember-i18n',

    initialize: function initialize(container, application) {
        application.inject('route', 'i18n', 'service:i18n');
    }
};
