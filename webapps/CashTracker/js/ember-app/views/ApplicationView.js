define('views/ApplicationView', [
    'ember',
    'text!templates/application.html',
    'controllers/ApplicationController'
], function(Ember, tpl, ApplicationController) {
    "use strict";

    var ApplicationView = Ember.View.extend({
        classNames: ['layout'],

        controller: ApplicationController,

        template: Ember.Handlebars.compile(tpl),

        willInsertElement: function() {
            $('.page-loading').hide();
        }
    });

    return ApplicationView;
});
