define('models/Account', ['underscore', 'backbone.localStorage'], function(_, Backbone) {

    var store = new Backbone.LocalStorage(window.store || 'Accounts');

    var Account = Backbone.Model.extend({
        url: '',
        localStorage: store,
        defaults: {
            name: ''
        },
        validate: function(attrs) {
            if (_.isEmpty(attrs.name)) {
                return 'Missing name';
            }
        }
    });

    var Accounts = Backbone.Collection.extend({
        localStorage: store,
        model: Account
    });

    return {
        Model: Account,
        Collection: Accounts
    };
});
