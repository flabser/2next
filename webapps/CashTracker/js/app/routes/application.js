CT.ApplicationRoute = Ember.Route.extend({
    actions: {
        willTransition: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
