CT.ApplicationRoute = Ember.Route.extend({

    init: function() {
        this.windowOnResize();
        $(window).resize(this.windowOnResize);
    },

    windowOnResize: function() {
        if (window.innerWidth <= 800) {
            $('body').addClass('phone');
        } else {
            $('body').removeClass('phone');
        }
    },

    model: function() {
        var route = this,
            sessionService = this.get('session');

        var req = sessionService.getSession();
        req.then(function(result) {
            if (result.authUser.login) {
                route.session.set('user', result.authUser);
                return result.authUser;
            }
        });
        return req;
    },

    actions: {
        logout: function() {
            var route = this;
            this.get('session').logout().then(function() {
                route.session.set('user', null);
                // route.transitionTo('index');
                window.location.href = 'Provider?id=welcome';
            });

        },

        navAppMenuToggle: function() {
            $('body').toggleClass('nav-app-open');
        },

        navUserMenuToggle: function() {
            $('body').toggleClass('nav-ws-open');
        },

        hideOpenedNav: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        },

        toggleSearchForm: function() {
            $('body').toggleClass('search-open');
        },

        error: function(error, transition) {
            console.log('app error', error);

            if (error.status === 401 || (!this.session.get('user') && this.routeName !== 'login')) {

                /*this.controllerFor('login').setProperties({
                    transition: transition
                });*/

                // this.transitionTo('login');
                // window.location.href = 'Provider?id=login' + location.hash;
            } else {
                return true;
            }
        },

        willTransition: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
