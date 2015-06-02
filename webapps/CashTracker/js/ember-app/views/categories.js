define('views/categories', [
    'ember',
    'text!templates/categories.html'
], function(Ember, tpl) {
    "use strict";

    var CategoriesView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return CategoriesView;
});
