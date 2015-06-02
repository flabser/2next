define('controllers/accounts', ['ember'], function(Ember) {
    "use strict";

    var Controller = Ember.Controller.extend({
        model: {
            username: 'Medet',
            logout: 'Logout'
        }
    });

    return Controller;
});
