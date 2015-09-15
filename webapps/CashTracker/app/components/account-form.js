import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

const noteMaxLen = 256;

export default Em.Component.extend(ModelForm, {
    account: null,
    users: null,

    noteCharacterLeft: Em.computed('account.note', function() {
        if (this.get('account.note')) {
            var noteLen = this.get('account.note').length;
            return noteMaxLen - noteLen;
        }
        return '';
    }),

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('account'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        },

        validateName: function() {
            this.validate('name');
        },
        validateCurrencyCode: function() {
            this.validate('currencyCode');
        },
        validateOpeningBalance: function() {
            this.validate('openingBalance');
        },
        validateAmountControl: function() {
            this.validate('amountControl');
        },
        validateNote: function() {
            this.validate('note');
        }
    },

    validate: function(fieldName) {
        var i18n = this.get('i18n');

        if (fieldName) {
            this.get('errors').remove(fieldName);

            switch (fieldName) {
                case 'name':
                    if (Validate.isEmpty(this.get('account.name'))) {
                        this.get('errors').add('name', i18n.t('validation_empty'));
                    }
                    break;
                case 'currencyCode':
                    if (Validate.isEmpty(this.get('account.currencyCode'))) {
                        this.get('errors').add('currencyCode', i18n.t('validation_empty'));
                    }
                    break;
                case 'openingBalance':
                    if (!Validate.isNumeric(this.get('account.openingBalance'))) {
                        this.get('errors').add('openingBalance', i18n.t('validation_numeric'));
                    }
                    break;
                case 'amountControl':
                    if (!Validate.isNumeric(this.get('account.amountControl'))) {
                        this.get('errors').add('amountControl', i18n.t('validation_numeric'));
                    }
                    break;
                case 'note':
                    if (this.get('account.note') && this.get('account.note').length > noteMaxLen) {
                        this.get('errors').add('note', i18n.t('validation_long', noteMaxLen));
                    }
                    break;
            }
        } else {
            this.set('errors', DS.Errors.create());

            if (Validate.isEmpty(this.get('account.name'))) {
                this.get('errors').add('name', i18n.t('validation_empty'));
            }
            if (Validate.isEmpty(this.get('account.currencyCode'))) {
                this.get('errors').add('currencyCode', i18n.t('validation_empty'));
            }
            if (!Validate.isNumeric(this.get('account.openingBalance'))) {
                this.get('errors').add('openingBalance', i18n.t('validation_numeric'));
            }
            if (!Validate.isNumeric(this.get('account.amountControl'))) {
                this.get('errors').add('amountControl', i18n.t('validation_numeric'));
            }
            if (this.get('account.note') && this.get('account.note.length') > noteMaxLen) {
                this.get('errors').add('note', i18n.t('validation_long', noteMaxLen));
            }
        }

        return this.get('errors.isEmpty');
    }
});
