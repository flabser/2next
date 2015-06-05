define('views/users', [
    'ember',
    'templates/users'
], function(Ember, tpl) {
    "use strict";

    var UsersView = Ember.View.extend({
        templateName: 'templates/users',
    });

    return UsersView;
});
