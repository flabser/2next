AdminApp.AppRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('app', params.app_id);
    }
});
