define('views/AccountsView', [
    'ember',
    'text!templates/accounts.html',
    'controllers/AccountsController'
], function(Ember, tpl, AccountsController) {
    "use strict";

    var AccountsView = Ember.View.extend({
        controller: AccountsController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return AccountsView;
});
