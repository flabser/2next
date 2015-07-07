CT.SessionService = Ember.Service.extend({

    getSession: function() {
        return $.getJSON('rest/session');
    },

    login: function(userName, password) {
        return $.ajax({
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
        return $.ajax({
            method: 'DELETE',
            url: 'rest/session'
        });
    }
});
