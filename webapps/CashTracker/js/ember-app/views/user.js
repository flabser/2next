define('views/user', [
    'ember',
    'templates/user'
], function(Ember, tpl) {
    "use strict";

    var UserView = Ember.View.extend({
        templateName: 'templates/user',
    });

    return UserView;
});
