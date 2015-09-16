import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

const noteMaxLen = 256;

export default Em.Component.extend(ModelForm, {
    category: null,
    transactionTypes: null,

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
