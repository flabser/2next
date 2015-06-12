CT.UsersController = Ember.ArrayController.extend();

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
