define('views/application', [
    'ember',
    'text!templates/application.html'
], function(Ember, tpl) {
    "use strict";

    var ApplicationView = Ember.View.extend({
        classNames: ['layout'],

        template: Ember.Handlebars.compile(tpl),

        willInsertElement: function() {
            $('.page-loading').hide();
        }
    });

    return ApplicationView;
});
