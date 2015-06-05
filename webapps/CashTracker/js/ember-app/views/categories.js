define('views/categories', [
    'ember',
    'templates/categories'
], function(Ember, tpl) {
    "use strict";

    var CategoriesView = Ember.View.extend({
        templateName: 'templates/categories',
    });

    return CategoriesView;
});
