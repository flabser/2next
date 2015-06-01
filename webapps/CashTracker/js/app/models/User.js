define('models/User', ['underscore', 'backbone.localStorage'], function(_, Backbone) {

    var store = new Backbone.LocalStorage(window.store || "Users");

    var User = Backbone.Model.extend({
        localStorage: store,
        defaults: {
            name: ""
        },
        validate: function(attrs) {
            if (_.isEmpty(attrs.name)) {
                return "Missing name";
            }
        }
    });

    return {
        Model: User
    };
});
