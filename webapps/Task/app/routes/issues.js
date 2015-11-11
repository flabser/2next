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
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('issues.new');
        },

        saveIssue: function(issue) {
            issue.save().then(() => {
                this.transitionTo('issues.issue', issue);
            }, function() {
                console.log(arguments);
            });
        },

        deleteIssue: function(issue) {
            issue.destroyRecord().then(() => {
                this.transitionTo('issues');
            }, function(resp) {
                issue.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
