import Em from 'ember';
import DS from 'ember-data';
import Validate from '../utils/validator';

export default Em.Component.extend({
    i18n: Em.inject.service(),
    errors: DS.Errors.create(),
    userProfile: null,

    willDestroyElement: function() {
        this.set('errors', null);
    },

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('save', this.get('userProfile'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        },

        addAttach: function(attach) {
            this.get('userProfile').attachedFile = attach;
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('userProfile.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
