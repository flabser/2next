CT.UsersController = Ember.ArrayController.extend({
    queryParams: ['offset', 'limit', 'order_by']
});

CT.UsersNewController = Ember.ArrayController.extend({
    actions: {
        create: function() {
            this.transitionTo('users.new');
        },
        save: function() {
            var newUser = this.store.createRecord('user', {
                name: this.get('name')
            });
            newUser.save();
        },
        cancel: function() {
            this.transitionTo('users');
        }
    }
});
