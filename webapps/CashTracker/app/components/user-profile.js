import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    userProfile: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('save', this.get('userProfile'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        },

        addAttach: function(attach) {
            this.get('userProfile').attachedFile = attach;
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('userProfile.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
