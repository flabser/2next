define('controllers/transaction', ['ember'], function(Ember) {
    "use strict";

    var Controller = Ember.Controller.extend({
        actions: {
            save: function() {
                alert('save');
            }
        }
    });

    return Controller;
});
