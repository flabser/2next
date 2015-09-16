import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    tag: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('tag'));
            }
        },

        close: function() {
            this.sendAction('close');
        },

        validateName: function() {
            this.validate();
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('tag.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
