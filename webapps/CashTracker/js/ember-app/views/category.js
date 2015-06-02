define('views/category', [
    'ember',
    'text!templates/category.html'
], function(Ember, tpl) {
    "use strict";

    var CategoryView = Ember.View.extend({
        template: Ember.Handlebars.compile(tpl)
    });

    return CategoryView;
});
