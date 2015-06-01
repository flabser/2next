define('views/CategoryView', [
    'ember',
    'text!templates/category.html',
    'controllers/CategoryController'
], function(Ember, tpl, CategoryController) {
    "use strict";

    var CategoryView = Ember.View.extend({
        controller: CategoryController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return CategoryView;
});
