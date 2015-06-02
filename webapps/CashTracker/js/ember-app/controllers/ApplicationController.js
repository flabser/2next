define('controllers/ApplicationController', ['ember'], function(Ember) {
    "use strict";

    var Controller = Ember.Controller.extend({
        init: function() {
            if (window.innerWidth <= 800) {
                $('body').addClass('phone');
            } else {
                $('body').removeClass('phone');
            }
        },

        model: {
            username: 'Medet',
            logout: 'Logout'
        },

        actions: {
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
            }
        }
    });

    return Controller;
});
