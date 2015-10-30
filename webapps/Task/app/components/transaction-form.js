import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/components/form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    transaction: null,
    accounts: null,
    categories: null,
    costCenters: null,
    tags: null,
    noteMaxLen: 256,

    mainAccounts: Em.computed('accounts', 'transaction.transferAccount', function() {
        let list = this.get('accounts');
        let ttaId = this.get('transaction.transferAccount.id');

        if (ttaId) {
            return list.filter((a) => {
                return a.get('enabled') && ttaId !== a.get('id');
            });
        } else {
            return list.filter((a) => {
                return a.get('enabled');
            });
        }
    }),

    transferAccounts: Em.computed('accounts', 'transaction.account', function() {
        let list = this.get('accounts');
        let taId = this.get('transaction.account.id');

        return list.filter((a) => {
            return a.get('enabled') && taId !== a.get('id');
        });
    }),

    categoriesByType: Em.computed('categories', 'transaction.transactionType', function() {
        let list = this.get('categories');
        let tType = this.get('transaction.transactionType') || false;

        return list.filter((c) => {
            if (tType) {
                return c.get('enabled') && tType === c.get('transactionType');
            } else {
                return c.get('enabled');
            }
        });
    }),

    isTransfer: Em.computed('transaction.transactionType', function() {
        return this.get('transaction.transactionType') === 'T';
    }),

    _catTrTypeObserver: Em.observer('transaction.category', function() {
        let ctt = this.get('transaction.category.transactionType');
        this.get('transaction').set('transactionType', ctt);
    }),

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('transaction'));
            }
        },

        close: function() {
            this.sendAction('close');
        },

        addAttach: function(attach) {
            return this.get('transaction.attachments').createRecord(attach);
        },
        removeAttach: function(tid) {
            this.get('transaction.attachments').forEach((attach) => {
                if (attach && tid === attach.get('tempID')) {
                    console.log(tid, attach);
                    attach.destroyRecord();
                }
            });
        },

        validateDate: function() {
            this.validate('date');
        },
        validateAmount: function() {
            this.validate('amount');
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
                case 'date':
                    if (!Validate.isDate(this.get('transaction.date'))) {
                        this.get('errors').add('date', i18n.t('validation_date'));
                    }
                    break;
                case 'amount':
                    if (!Validate.isNumeric(this.get('transaction.amount'))) {
                        this.get('errors').add('amount', i18n.t('validation_numeric'));
                    }
                    break;
                case 'note':
                    if (this.get('transaction.note') && this.get('transaction.note').length > noteMaxLen) {
                        this.get('errors').add('note', i18n.t('validation_long', noteMaxLen));
                    }
                    break;
            }
        } else {
            this.set('errors', DS.Errors.create());

            if (!Validate.isDate(this.get('transaction.date'))) {
                this.get('errors').add('date', i18n.t('validation_date'));
            }
            if (!Validate.isNumeric(this.get('transaction.amount'))) {
                this.get('errors').add('amount', i18n.t('validation_numeric'));
            }
            if (this.get('transaction.note') && this.get('transaction.note').length > noteMaxLen) {
                this.get('errors').add('note', i18n.t('validation_long', noteMaxLen));
            }
        }

        return this.get('errors.isEmpty');
    }
});
