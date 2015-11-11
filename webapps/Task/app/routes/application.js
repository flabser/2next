import Em from 'ember';

const {
    Route, inject, $
} = Em;

export default Route.extend({
    showAddAction: false,

    session: inject.service(),
    translationsFetcher: inject.service(),

    activate: function() {
        this.windowOnResize();
        $(window).resize(this.windowOnResize);
    },

    model: function() {
        var sessionService = this.get('session');
        return sessionService.getSession().then(() => sessionService.get('user'));
    },

    afterModel: function() {
        return this.get('translationsFetcher').fetch();
    },

    setupController: function(controller, model) {
        controller.set('sessionUser', model);
        // loginThroughToken - show link rest/workspace/url if LOGIN_THROUGH_TOKEN
        controller.set('loginThroughToken', model.authMode === 'LOGIN_THROUGH_TOKEN');

        setTimeout(this.initScrollSpySide, 200);
    },

    windowOnResize: function() {
        if (window.innerWidth <= 768) {
            $('body').addClass('phone');
        } else {
            $('body').removeClass('phone');
        }
    },

    initScrollSpySide: function() {

        $('body').removeClass('no_transition');
        $('#app-loading').hide();

        // init dropdown
        $('[data-toggle=dropdown]').click(function() {
            let $dropdown = $(this).parent('.dropdown');
            if ($dropdown.hasClass('open')) {
                $dropdown.removeClass('open');
            } else {
                $dropdown.addClass('open');
                setTimeout(function() {
                    $('body').one('click', function() {
                        $dropdown.removeClass('open');
                    });
                }, 100);
            }
        });

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

    actions: {
        logout: function() {
            this.get('session').logout().then(function(response) {
                window.location.href = response.outcome.message[0];
            });
        },

        goBack: function() {
            window.history.back();
        },

        transitionToIssues: function() {
            this.transitionTo('issues');
        },

        transitionToTags: function() {
            this.transitionTo('settings.tags');
        },

        transitionToUsers: function() {
            this.transitionTo('settings.users');
        },

        showAddAction: function() {
            this.set('showAddAction', true);
        },

        hideAddAction: function() {
            this.set('showAddAction', false);
        },

        navAppMenuToggle: function() {
            $('body').toggleClass('nav-app-open');
        },

        hideOpenedNav: function() {
            $('body').removeClass('nav-app-open');
        },

        toggleSearchForm: function() {
            $('body').toggleClass('search-open');
        },

        willTransition: function(transition) {
            $('body').removeClass('nav-app-open');
            $('.dropdown.open').removeClass('open');
        },

        setWindowTitle: function(title) {
            if (title.length > 0 && top.document.title != title) {
                top.document.title = title;
            }
        },

        error: function(_error, transition) {
            console.log(_error);

            if (_error.errors && _error.errors.length && _error.errors[0].status === '401') {
                window.location.href = '//' + location.host;
            }

            if (_error.status === 401 || (!this.get('session').isAuthenticated() && this.routeName !== 'login')) {
                window.location.href = '//' + location.host;
            } else if (_error.status === '400') {
                return true;
            } else {
                return true;
            }
        }
    }
});
