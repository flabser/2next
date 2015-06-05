define('views/category', [
    'ember',
    'templates/category'
], function(Ember, tpl) {
    "use strict";

    var CategoryView = Ember.View.extend({
        templateName: 'templates/category',
    });

    return CategoryView;
});
