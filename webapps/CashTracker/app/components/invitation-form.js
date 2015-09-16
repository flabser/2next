import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

const noteMaxLen = 256;

export default Em.Component.extend(ModelForm, {
    invitation: null,

    actions: {
        sendInvite: function() {
            if (this.validate()) {
                this.sendAction('sendInvite', this.get('invitation'));
            }
        },

        close: function() {
            this.sendAction('close');
        },

        validateEmail: function() {
            this.validate('email');
        },
        validateMessage: function() {
            this.validate('message');
        }
    },

    validate: function(fieldName) {
        var i18n = this.get('i18n');

        if (fieldName) {
            this.get('errors').remove(fieldName);

            switch (fieldName) {
                case 'email':
                    if (!Validate.isEmail(this.get('invitation.email'))) {
                        this.get('errors').add('email', i18n.t('validation_email'));
                    }
                    break;
                case 'message':
                    if (Validate.isEmpty(this.get('invitation.message'))) {
                        this.get('errors').add('message', i18n.t('validation_empty'));
                    }
                    break;
            }
        } else {
            this.set('errors', DS.Errors.create());

            if (!Validate.isEmail(this.get('invitation.email'))) {
                this.get('errors').add('email', i18n.t('validation_email'));
            }
            if (Validate.isEmpty(this.get('invitation.message'))) {
                this.get('errors').add('message', i18n.t('validation_empty'));
            }
        }

        return this.get('errors.isEmpty');
    }
});
