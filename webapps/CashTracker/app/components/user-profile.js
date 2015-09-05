import Em from 'ember';
import DS from 'ember-data';

export default Em.Component.extend({
    errors: DS.Errors.create(),

    userProfile: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveUserProfile', this.get('userProfile'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (!this.get('userProfile.name')) {
            this.get('errors').add('name', 'can_not_be_empty');
        }

        return this.get('errors.isEmpty');
    }
});
