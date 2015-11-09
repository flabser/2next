import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return Em.RSVP.hash({
            tags: this.store.findAll('tag'),
            users: this.store.findAll('user')
        });
    },

    setupController: function(controller, model) {
        controller.set('tags', model.tags);
        controller.set('users', model.users);
    }
});
