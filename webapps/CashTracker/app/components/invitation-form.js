import Em from 'ember';
import DS from 'ember-data';
import Validate from '../utils/validator';

const noteMaxLen = 256;

export default Em.Component.extend({
    i18n: Em.inject.service(),
    errors: DS.Errors.create(),
    invitation: null,

    noteCharacterLeft: Em.computed('category.note', function() {
        if (this.get('category.note')) {
            return noteMaxLen - this.get('category.note').length;
        }
        return '';
    }),

    actions: {
        sendInvite: function() {
            if (this.validate()) {
                this.sendAction('sendInvite', this.get('invitation'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        },

        validateEmail: function() {
            this.validate('email');
        },
        validateMessage: function() {
            this.validate('message');
        }
    },

    validate: function(fieldName) {
        if (fieldName) {
            this.get('errors').remove(fieldName);

            switch (fieldName) {
                case 'email':
                    if (!Validate.isEmail(this.get('invitation.email'))) {
                        this.get('errors').add('email', this.get('i18n').t('validation_email'));
                    }
                    break;
                case 'message':
                    if (Validate.isEmpty(this.get('invitation.message'))) {
                        this.get('errors').add('message', this.get('i18n').t('validation_empty'));
                    }
                    break;
            }
        } else {
            this.set('errors', DS.Errors.create());

            if (!Validate.isEmail(this.get('invitation.email'))) {
                this.get('errors').add('email', this.get('i18n').t('validation_email'));
            }
            if (Validate.isEmpty(this.get('invitation.message'))) {
                this.get('errors').add('message', this.get('i18n').t('validation_empty'));
            }
        }

        return this.get('errors.isEmpty');
    }
});
