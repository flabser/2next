import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    user: null,
    changePassword: false,
    isEdit: false,
    userEmail: '',

    init: function() {
        this._super();

        var um = this.get('user.email');
        this.set('userEmail', um);
    },

    needPassword: Em.computed('changePassword', 'user.email', function() {
        if (this.get('changePassword') === true) {
            return true;
        }

        return !Validate.isEmpty(this.get('user.email')) && this.get('user.email') !== this.get('userEmail');
    }),

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
        setChangePassword: function() {
            this.set('changePassword', true);
        },
        setEdit: function() {
            this.set('isEdit', true);
        },
        validateProfile: function() {
            this.validate();
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('user.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }
        if (Validate.isEmpty(this.get('user.login'))) {
            this.get('errors').add('login', this.get('i18n').t('validation_empty'));
        }
        if (Validate.isEmpty(this.get('user.email'))) {
            this.get('errors').add('email', this.get('i18n').t('validation_empty'));
        }

        if (this.get('changePassword')) {
            if (!Validate.isEmpty(this.get('user.pwd_new')) && !Validate.isEmpty(this.get('user.pwd_repeat'))) {
                if (Validate.isEmpty(this.get('user.pwd'))) {
                    this.get('errors').add('pwd', this.get('i18n').t('validation_empty'));
                }
            }

            if (!Validate.isEmpty(this.get('user.pwd_new')) || !Validate.isEmpty(this.get('user.pwd_repeat'))) {
                if (!Validate.notEmptyAndEqual(this.get('user.pwd_new'), this.get('user.pwd_repeat'))) {
                    this.get('errors').add('pwd_change', this.get('i18n').t('pwd_repeat_not_equal'));
                }
            }
        }

        if (!Validate.isEmpty(this.get('user.email')) && this.get('user.email') !== this.get('userEmail')) {
            if (Validate.isEmpty(this.get('user.pwd'))) {
                this.get('errors').remove('pwd');
                this.get('errors').add('pwd', this.get('i18n').t('validation_empty'));
            }
        }


        return this.get('errors.isEmpty');
    }
});
