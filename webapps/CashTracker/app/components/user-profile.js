import Em from 'ember';
import DS from 'ember-data';
import ModelFormMixin from '../mixins/components/form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelFormMixin, {
    user: null,
    changePassword: false,
    isEdit: false,
    userEmail: '',
    avatarUrl: 'rest/session/avatar',

    refresh: 'refresh',

    session: Em.inject.service(),

    init: function() {
        this._super();
        this.set('userEmail', this.get('user.email'));
    },

    willDestroyElement: function() {
        this.set('user.pwd', '');
        this.set('user.pwd_new', '');
        this.set('user.pwd_repeat', '');
    },

    needPassword: Em.computed('changePassword', 'user.email', function() {
        if (this.get('changePassword') === true) {
            return true;
        }

        return !Validate.isEmpty(this.get('user.email')) && this.get('user.email') !== this.get('userEmail');
    }),

    actions: {
        edit: function() {
            this.set('isEdit', true);
        },

        save: function() {
            if (this.validate()) {
                this.send('saveUserProfile');
            }
        },

        saveUserProfile: function() {
            this.get('session').saveUserProfile().then(() => {
                this.set('avatarUrl', 'rest/session/avatar?' + Date.now());
            });
        },

        close: function() {
            this.sendAction('close');
        },

        updateAvatar: function(attach) {
            let u = this.get('user');

            try {
                delete u.attachedFile;
            } catch (e) {}

            u.attachedFile = attach;
        },

        setChangePassword: function() {
            this.set('changePassword', true);
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
            var pwdNew = this.get('user.pwd_new');
            var pwdRepeat = this.get('user.pwd_repeat');

            if (!Validate.isEmpty(pwdNew) && !Validate.isEmpty(pwdRepeat)) {
                if (Validate.isEmpty(this.get('user.pwd'))) {
                    this.get('errors').add('pwd', this.get('i18n').t('validation_empty'));
                }
            }

            if (!Validate.isEmpty(pwdNew) || !Validate.isEmpty(pwdRepeat)) {
                if ((pwdNew + pwdRepeat).indexOf(' ') > -1) {
                    this.get('errors').add('pwd_format', this.get('i18n').t('pwd_format_error'));
                } else {
                    if (!Validate.notEmptyAndEqual(pwdNew, pwdRepeat)) {
                        this.get('errors').add('pwd_change', this.get('i18n').t('pwd_repeat_not_equal'));
                    }
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
