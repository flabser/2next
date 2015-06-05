define('views/accounts', [
    'ember',
    'templates/accounts'
], function(Ember, tpl) {
    "use strict";

    var AccountsView = Ember.View.extend({
        templateName: 'templates/accounts',
    });

    return AccountsView;
});
