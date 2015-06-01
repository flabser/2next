define('models/Category', ['underscore', 'backbone.localStorage'], function(_, Backbone) {

    var store = new Backbone.LocalStorage(window.store || 'Categories');

    var Category = Backbone.Model.extend({
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

    var Categories = Backbone.Collection.extend({
        localStorage: store,
        model: Category
    });

    return {
        Model: Category,
        Collection: Categories
    };
});
