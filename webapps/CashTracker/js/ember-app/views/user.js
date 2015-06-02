define('views/user', [
    'ember',
    'text!templates/user.html'
], function(Ember, tpl) {
    "use strict";

    var UserView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return UserView;
});
