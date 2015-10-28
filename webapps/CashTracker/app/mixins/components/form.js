import Em from 'ember';
import DS from 'ember-data';

export default Em.Mixin.create({
    i18n: Em.inject.service(),
    errors: DS.Errors.create(),

    willInsertElement: function() {
        this.set('errors', DS.Errors.create());
    },

    willDestroyElement: function() {
        this.set('errors', null);
    }
});
