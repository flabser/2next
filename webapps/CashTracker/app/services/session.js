import Ember from 'ember';

const PATH = 'rest/session';

export default Ember.Service.extend({

    _isAuthenticated: false,

    isAuthenticated: function() {
        return this._isAuthenticated;
    },

    getSession: function() {
        return Ember.$.get(PATH).then(this._setResult.bind(this));
    },

    _setResult: function(result) {
        this.set('user', result.authUser);
    },

    login: function(userName, password) {
        const _this = this;

        return Ember.$.ajax({
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
                _this._isAuthenticated = true;
                return result;
            }
        });
    },

    logout: function() {
        this._isAuthenticated = false;
        return Ember.$.ajax({
            method: 'DELETE',
            url: PATH
        });
    }
});
