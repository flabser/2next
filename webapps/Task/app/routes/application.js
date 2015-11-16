import Em from 'ember';

const {
    Route, inject, $
} = Em;

export default Route.extend({
    session: inject.service(),
    translationsFetcher: inject.service(),

    activate: function() {
        this.windowOnResize();
        $(window).resize(this.windowOnResize);
    },

    model: function() {
        var sessionService = this.get('session');
        return Em.RSVP.hash({
            sessionUser: sessionService.getSession().then(() => sessionService.get('user')),
            tags: this.store.findAll('tag'),
            users: this.store.findAll('user')
        });
    },

    afterModel: function() {
        return this.get('translationsFetcher').fetch();
    },

    setupController: function(controller, model) {
        controller.set('tags', model.tags);
        controller.set('users', model.users);
        controller.set('sessionUser', model.sessionUser);
        // loginThroughToken - show link rest/workspace/url if LOGIN_THROUGH_TOKEN
        controller.set('loginThroughToken', model.sessionUser.authMode === 'LOGIN_THROUGH_TOKEN');

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

        $('[data-toggle=panel]').click(function() {
            let $panel = $(this).parents('.panel');
            if ($panel.hasClass('open')) {
                $panel.removeClass('open');
            } else {
                $panel.addClass('open');
            }
        });

        var offsetTop = 0;
        var sideOnTop = false;
        var $side = $('#side-nav----');

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
            this.transitionTo('tags');
        },

        transitionToUsers: function() {
            this.transitionTo('users');
        },

        showAddAction: function() {
            this.set('showAddAction', true);
        },

        hideAddAction: function() {
            this.set('showAddAction', false);
        },

        navAppMenuToggle: function() {
            $('body').toggleClass('side-nav-open');
        },

        hideOpenedNav: function() {
            $('body').removeClass('side-nav-open');
        },

        toggleSearchForm: function() {
            $('body').toggleClass('search-open');
        },

        willTransition: function(transition) {
            $('body').removeClass('side-nav-open');
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
