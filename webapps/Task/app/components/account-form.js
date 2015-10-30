import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/components/form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    account: null,
    users: null,
    currencies: null,
    noteMaxLen: 256,

    currencyCode: Em.computed('account', function() {
        return this.get('account.currencyCode');
    }),

    actions: {
        save: function() {
            let code = this.get('currencyCode');
            this.set('account.currencyCode', code);

            if (this.validate()) {
                this.sendAction('saveRecord', this.get('account'));
            }
        },

        close: function() {
            this.sendAction('close');
        },

        selectizeInitCurrencyAction: function() {
            let code = this.get('currencyCode');
            this.set('account.currencyCode', code);
        },

        selectizeSelectCurrencyItemAction: function(selection) {
            this.get('account').set('currencyCode', (selection ? selection.code : ''));
            this.validate('currencyCode');
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
        const noteMaxLen = this.noteMaxLen;
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
