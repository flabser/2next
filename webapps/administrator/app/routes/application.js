AdminApp.ApplicationRoute = Ember.Route.extend({
    templateName: 'application',

    model: function() {
        return [{
            title: "Logs",
            viewdata: "logs"
        }, {
            title: "Users",
            viewdata: "users"
        }, {
                     title: "Applications",
                     viewdata: "apps"
            }];
    }
});
