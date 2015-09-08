import Em from 'ember';
import DS from 'ember-data';
import Validate from '../utils/validator';

const noteMaxLen = 256;

export default Em.Component.extend({
    i18n: Em.inject.service(),
    errors: DS.Errors.create(),
    category: null,
    transactionTypes: null,

    noteCharacterLeft: Em.computed('category.note', function() {
        if (this.get('category.note')) {
            return noteMaxLen - this.get('category.note').length;
        }
        return '';
    }),

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('category'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        },

        validateName: function() {
            this.validate('name');
        },
        validateNote: function() {
            this.validate('note');
        },
    },

    validate: function(fieldName) {
        if (fieldName) {
            this.get('errors').remove(fieldName);

            switch (fieldName) {
                case 'name':
                    if (Validate.isEmpty(this.get('category.name'))) {
                        this.get('errors').add('name', this.get('i18n').t('validation_empty'));
                    }
                    break;
                case 'note':
                    if (this.get('category.note') && this.get('category.note').length > noteMaxLen) {
                        this.get('errors').add('note', this.get('i18n').t('validation_long', noteMaxLen));
                    }
                    break;
            }
        } else {
            this.set('errors', DS.Errors.create());

            if (Validate.isEmpty(this.get('category.name'))) {
                this.get('errors').add('name', this.get('i18n').t('validation_empty'));
            }
            if (this.get('category.note') && this.get('category.note.length') > noteMaxLen) {
                this.get('errors').add('note', this.get('i18n').t('validation_long', noteMaxLen));
            }
        }

        return this.get('errors.isEmpty');
    }
});
