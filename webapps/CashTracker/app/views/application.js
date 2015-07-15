import Ember from 'ember';

export default Ember.View.extend({
    classNames: ['layout'],

    willInsertElement: function() {
        Ember.$('.page-loading').hide();
    }
});
