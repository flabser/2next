import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/components/form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    category: null,
    categories: null,
    noteMaxLen: 256,

    parentCategories: Em.computed('categories', 'category.transactionType', function() {
        let list = this.get('categories');
        let transactionType = this.get('category.transactionType');
        let categoryId = this.get('category.id') || -1;

        return list.filter((c) => {
            let c_isNew = c.get('isNew');
            let c_id = c.get('id');

            if (transactionType) {
                let c_tt = c.get('transactionType');
                return !c_isNew && c.get('enabled') && c_tt === transactionType && c_id !== categoryId;
            } else {
                return !c_isNew && c.get('enabled') && c_id !== categoryId;
            }
        });
    }),

    _parentCatTrTypeObserver: Em.observer('category.parent', function() {
        let pctt = this.get('category.parent.transactionType');
        this.get('category').set('transactionType', pctt);
    }),

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('category'));
            }
        },

        close: function() {
            this.sendAction('close');
        },

        validateName: function() {
            this.validate('name');
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
                    if (Validate.isEmpty(this.get('category.name'))) {
                        this.get('errors').add('name', i18n.t('validation_empty'));
                    }
                    break;
                case 'note':
                    if (this.get('category.note') && this.get('category.note').length > noteMaxLen) {
                        this.get('errors').add('note', i18n.t('validation_long', noteMaxLen));
                    }
                    break;
            }
        } else {
            this.set('errors', DS.Errors.create());

            if (Validate.isEmpty(this.get('category.transactionType'))) {
                this.get('errors').add('transactionType', i18n.t('validation_empty'));
            }
            if (Validate.isEmpty(this.get('category.name'))) {
                this.get('errors').add('name', i18n.t('validation_empty'));
            }
            if (this.get('category.note') && this.get('category.note.length') > noteMaxLen) {
                this.get('errors').add('note', i18n.t('validation_long', noteMaxLen));
            }
        }

        return this.get('errors.isEmpty');
    }
});
