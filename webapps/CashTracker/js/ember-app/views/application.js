define('views/application', [
    'ember',
    'templates/application'
], function(Ember, tpl) {
    "use strict";

    var ApplicationView = Ember.View.extend({
        classNames: ['layout'],

        templateName: 'templates/application',

        willInsertElement: function() {
            $('.page-loading').hide();
        }
    });

    return ApplicationView;
});
