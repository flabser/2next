define('views/users', [
    'ember',
    'text!templates/users.html'
], function(Ember, tpl) {
    "use strict";

    var UsersView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return UsersView;
});
