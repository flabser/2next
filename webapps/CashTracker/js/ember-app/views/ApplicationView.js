define('views/ApplicationView', [
    'ember',
    'text!templates/application.html',
    'controllers/ApplicationController'
], function(Ember, tpl, ApplicationController) {
    "use strict";

    var ApplicationView = Ember.View.extend({
        classNames: ['layout'],
        controller: ApplicationController,
        defaultTemplate: Ember.Handlebars.compile(tpl)
    });

    return ApplicationView;
});
