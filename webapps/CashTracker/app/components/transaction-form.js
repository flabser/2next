import Em from 'ember';
import DS from 'ember-data';
import Validate from '../utils/validator';

export default Em.Component.extend({
    i18n: Em.inject.service(),
    errors: DS.Errors.create(),
    transaction: null,

    isTransfer: Em.computed('transaction.transactionType', function() {
        return this.get('transaction.transactionType') === 'T';
    }),

    noteCharacterLeft: Em.computed('transaction.note', function() {
        if (this.get('transaction.note')) {
            return 256 - this.get('transaction.note').length;
        }
        return '';
    }),

    willDestroyElement: function() {
        this.set('errors', null);
    },

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('transaction'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        },

        addAttach: function(attach) {
            this.get('transaction.attachments').createRecord(attach);
        },

        validateDate: function() {
            this.validate('date');
        },
        validateAmount: function() {
            this.validate('amount');
        },
        validateNote: function() {
            this.validate('note');
        },
    },

    validate: function(fieldName) {
        if (fieldName) {
            this.get('errors').remove(fieldName);

            switch (fieldName) {
                case 'date':
                    if (!Validate.isDate(this.get('transaction.date'))) {
                        this.get('errors').add('date', this.get('i18n').t('validation_date'));
                    }
                    break;
                case 'amount':
                    if (!Validate.isNumeric(this.get('transaction.amount'))) {
                        this.get('errors').add('amount', this.get('i18n').t('validation_numeric'));
                    }
                    break;
                case 'note':
                    if (this.get('transaction.note') && this.get('transaction.note').length > 256) {
                        this.get('errors').add('note', this.get('i18n').t('validation_long', 256));
                    }
                    break;
            }
        } else {
            this.set('errors', DS.Errors.create());

            if (!Validate.isDate(this.get('transaction.date'))) {
                this.get('errors').add('date', this.get('i18n').t('validation_date'));
            }
            if (!Validate.isNumeric(this.get('transaction.amount'))) {
                this.get('errors').add('amount', this.get('i18n').t('validation_numeric'));
            }
            if (this.get('transaction.note') && this.get('transaction.note').length > 256) {
                this.get('errors').add('note', this.get('i18n').t('validation_long', 256));
            }
        }

        return this.get('errors.isEmpty');
    }
});
