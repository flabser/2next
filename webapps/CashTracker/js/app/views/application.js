CT.ApplicationView = Ember.View.extend({
    classNames: ['layout'],

    templateName: 'application',

    willInsertElement: function() {
        $('.page-loading').hide();
    }
});
