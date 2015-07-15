import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'user',

    actions: {
        create: function() {
            this.transitionTo('users.new');
        },
        save: function() {
            var controller = this.controller;
            var newUser = this.store.createRecord('user', {
                name: controller.get('name')
            });
            newUser.save();
        },
        cancel: function() {
            this.transitionTo('users');
        }
    }
});
