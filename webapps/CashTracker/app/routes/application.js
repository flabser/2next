import Ember from 'ember';

export default Ember.Route.extend({

    init: function() {
        this.windowOnResize();
        Ember.$(window).resize(this.windowOnResize);
    },

    fetchTranslations: function() {
        return Ember.$.getJSON('rest/page/app-captions').then(function(data) {
            return data._Page.captions;
        });
    },

    windowOnResize: function() {
        if (window.innerWidth <= 800) {
            Ember.$('body').addClass('phone');
        } else {
            Ember.$('body').removeClass('phone');
        }
    },

    beforeModel: function() {
        var i18n = this.get('i18n');
        this.fetchTranslations().then(function(translations) {
            i18n.set('translations', translations);
        });
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

        historyBack: function() {
            history.back(-1);
        },

        navAppMenuToggle: function() {
            Ember.$('body').toggleClass('nav-app-open');
        },

        navUserMenuToggle: function() {
            Ember.$('body').toggleClass('nav-ws-open');
        },

        hideOpenedNav: function() {
            Ember.$('body').removeClass('nav-app-open nav-ws-open');
        },

        toggleSearchForm: function() {
            Ember.$('body').toggleClass('search-open');
        },

        error: function(error, transition) {
            if (error.status === 401 || (!this.session.get('user') && this.routeName !== 'login')) {
                window.location.href = 'Provider?id=login';
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
            Ember.$('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
