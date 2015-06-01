define('views/CategoriesView', [
    'ember',
    'text!templates/categories.html',
    'controllers/CategoriesController'
], function(Ember, tpl, CategoriesController) {
    "use strict";

    var CategoriesView = Ember.View.extend({
        controller: CategoriesController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return CategoriesView;
});
