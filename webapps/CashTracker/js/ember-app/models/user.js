define('models/user', ['ember'], function(Ember) {
    "use strict";

    var Model = Ember.Object.extend({
        name: 'User model'
    });

    return Model;
});
