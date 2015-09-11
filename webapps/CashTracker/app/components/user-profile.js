import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    user: null,
    changePassword: false,
    needPassword: false,
    isEdit: false,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('save', this.get('user'));
            }
        },
        cancel: function() {
            this.sendAction('cancel');
        },
        addAttach: function(attach) {
            this.get('user').avatar = attach;
        },
        toggleChangePassword: function() {
            this.set('changePassword', !this.get('changePassword'));
            this.set('needPassword', this.get('changePassword'));
        },
        setEdit: function() {
            this.set('isEdit', true);
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('user.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
