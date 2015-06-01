define('controllers/ApplicationController', ['ember'], function(Ember) {
    "use strict";

    var Controller = Ember.Controller.extend({
        username: 'Medet',
        logout: 'Logout',
        actions: {
            navAppMenuToggle: function() {
                $('body').toggleClass('nav-app-open');
            },
            navUserMenuToggle: function() {
                $('body').toggleClass('nav-ws-open');
            },
            hideOpenedNav: function() {
                $('body').removeClass('nav-app-open nav-ws-open');
            }
        },
        customEvents: {
        	click: function(){
        		alert('ok');
        	}
        }
    });

    return Controller;
});
