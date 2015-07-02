CT.ApplicationView = Ember.View.extend({
    classNames: ['layout'],

    willInsertElement: function() {
        $('.page-loading').hide();
    }
});
