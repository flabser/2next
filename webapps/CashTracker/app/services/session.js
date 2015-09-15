import Em from 'ember';

const {
    Service, $
} = Em;

const PATH = 'rest/session';

export default Service.extend({

    _isAuthenticated: false,

    isAuthenticated: function() {
        return this._isAuthenticated;
    },

    getSession: function() {
        return $.get(PATH).then(this._setResult.bind(this));
    },

    _setResult: function(result) {
        if (result.authUser.login) {
            result.authUser.pwd = '';
            result.authUser.pwd_new = '';
            result.authUser.pwd_repeat = '';
            this.set('user', result.authUser);
            this.set('_isAuthenticated', true);
        } else {
            this.set('_isAuthenticated', false);
        }
    },

    saveUserProfile: function() {
        let _this = this;

        return $.ajax({
            method: 'PUT',
            dataType: 'json',
            contentType: 'application/json',
            url: PATH,
            data: JSON.stringify({
                authUser: {
                    name: _this.user.name,
                    login: _this.user.login,
                    email: _this.user.email,
                    pwd: _this.user.password,
                    pwd_repeat: _this.user.password_repeat,
                    attachedFile: _this.user.attachedFile
                }
            }),
            success: function(result) {
                _this.set('_isAuthenticated', true);
                return result;
            }
        });
    },

    login: function(userName, password) {
        const _this = this;

        return $.ajax({
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: PATH,
            data: JSON.stringify({
                authUser: {
                    login: userName,
                    pwd: password
                }
            }),
            success: function(result) {
                _this.set('_isAuthenticated', true);
                return result;
            }
        });
    },

    logout: function() {
        this.set('_isAuthenticated', false);
        this.set('user', null);
        return $.ajax({
            method: 'DELETE',
            url: PATH
        });
    }
});
