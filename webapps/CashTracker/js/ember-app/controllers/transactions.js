define('controllers/transactions', ['ember'], function(Ember) {
    "use strict";

    var Controller = Ember.Controller.extend({
        actions: {
            addNew: function() {
                alert('click');
            }
        }
    });

    return Controller;
});
