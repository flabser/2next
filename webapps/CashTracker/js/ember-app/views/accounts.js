define('views/accounts', [
    'ember',
    'text!templates/accounts.html'
], function(Ember, tpl) {
    "use strict";

    var AccountsView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return AccountsView;
});
