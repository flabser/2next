define('views/UserView', [
    'ember',
    'text!templates/user.html',
    'controllers/UserController'
], function(Ember, tpl, UserController) {
    "use strict";

    var UserView = Ember.View.extend({
        controller: UserController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return UserView;
});
