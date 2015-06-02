define('views/account', [
    'ember',
    'text!templates/account.html'
], function(Ember, tpl) {
    "use strict";

    var AccountView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return AccountView;
});
