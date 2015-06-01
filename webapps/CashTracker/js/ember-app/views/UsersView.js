define('views/UsersView', [
    'ember',
    'text!templates/users.html',
    'controllers/UsersController'
], function(Ember, tpl, UsersController) {
    "use strict";

    var UsersView = Ember.View.extend({
        controller: UsersController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return UsersView;
});
