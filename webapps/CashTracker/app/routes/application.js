import Em from 'ember';

const {
    Route, inject, $
} = Em;

export default Route.extend({
    navProfileIsExpanded: false,
    hasAddAction: false,
    // loginThroughToken: false,

    session: inject.service(),

    translationsFetcher: inject.service(),

    activate: function() {
        this.windowOnResize();
        $(window).resize(this.windowOnResize);
    },

    windowOnResize: function() {
        if (window.innerWidth <= 768) {
            $('body').addClass('phone');
        } else {
            $('body').removeClass('phone');
        }
    },

    beforeModel: function() {
        return this.get('translationsFetcher').fetch();
    },

    model: function() {
        var sessionService = this.get('session');
        return sessionService.getSession().then(function() {
            return sessionService.get('user');
        });
    },

    afterModel: function(user) {
        // this.set('i18n.locale', user.get('locale'));
        if (!this.get('session').isAuthenticated()) {
            // window.location.href = 'Provider?id=login';
        } else {
            if (this.get('session').get('user.redirect') === 'setup') {
                this.controllerFor('budget').set('isEditMode', true);
                this.transitionTo('budget');
            } else {
                // this.controllerFor('budget').send('check');
            }
        }
    },

    setupController: function(controller, model) {
        controller.set('model', model);
        controller.set('loginThroughToken', model.authMode === 'LOGIN_THROUGH_TOKEN');

        setTimeout(this.initScrollSpySide, 200);
    },

    initScrollSpySide: function() {

        $('body').removeClass('no_transition');
        $('#app-loading').hide();

        var offsetTop = 0;
        var sideOnTop = false;
        var $side = $('#nav-app');

        if ($side.length) {
            offsetTop = $('.header')[0].clientHeight;

            $(window).scroll(scrollSpySide);
            scrollSpySide();
        }

        function scrollSpySide() {
            if (window.pageYOffset > offsetTop) {
                if (!sideOnTop) {
                    $side.css('top', '0px');
                    sideOnTop = true;
                }
            } else {
                if (sideOnTop) {
                    $side.css('top', '');
                    sideOnTop = false;
                }
            }
        }
    },

    setWindowTitle: function(title) {
        this.get('controller').set('windowTitle', title);
    },

    actions: {
        logout: function() {
            // var route = this;
        	this.get('session').logout().then(function(response) {
                // route.transitionTo('index');
            	//console.log(response.outcome.message[0]);
                window.location.href = response.outcome.message[0]
          });
        },

        goBack: function() {
            history.back(-1);
        },

        toggleDevice: function() {
            $('body').toggleClass('phone');
        },

        showAddAction: function() {
            this.set('hasAddAction', true);
        },

        hideAddAction: function() {
            this.set('hasAddAction', false);
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

        toggleNavProfile: function() {
            $('.nav-profile').toggleClass('expanded');
        },

        willTransition: function(transition) {
            $('body').removeClass('nav-app-open nav-ws-open');
        },

        error: function(_error /*, transition*/ ) {
            console.log(_error);

            if (_error.errors && _error.errors.length && _error.errors[0].status === '401') {
                // window.location.href = 'Provider?id=login';
            }

            if (_error.status === 401 || (!this.get('session').isAuthenticated() && this.routeName !== 'login')) {
                window.location.href = '/CashTracker/Provider?id=login';

                /*this.controllerFor('login').setProperties({
                    transition: transition
                });*/

                // this.transitionTo('login');
            } else if (_error.status === '400') {
                return true;
            } else {
                return true;
            }
        }
    }
});
