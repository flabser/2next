export default {
    name: 'session',

    initialize: function initialize(container, application) {
        application.inject('route', 'session', 'service:session');
    }
};
