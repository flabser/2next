export function initialize(container, application) {
    application.inject('route', 'session', 'service:session');
}

export default {
    name: 'session',
    initialize: initialize
};
