define('views/AccountView', [
    'ember',
    'text!templates/account.html',
    'controllers/AccountController'
], function(Ember, tpl, AccountController) {
    "use strict";

    var AccountView = Ember.View.extend({
        controller: AccountController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return AccountView;
});
