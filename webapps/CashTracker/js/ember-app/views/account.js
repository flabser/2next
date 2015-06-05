define('views/account', [
    'ember',
    'templates/account'
], function(Ember, tpl) {
    "use strict";

    var AccountView = Ember.View.extend({
        templateName: 'templates/account',
    });

    return AccountView;
});
