import Ember from 'ember';

export default Ember.Service.extend({

    getSession: function() {
        return Ember.$.getJSON('rest/session');
    },

    login: function(userName, password) {
        return Ember.$.ajax({
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: 'rest/session',
            data: JSON.stringify({
                authUser: {
                    login: userName,
                    pwd: password
                }
            }),
            success: function(result) {
                return result;
            }
        });
    },

    logout: function() {
        return Ember.$.ajax({
            method: 'DELETE',
            url: 'rest/session'
        });
    }
});
