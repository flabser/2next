import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('issue');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('issues.new');
        },

        saveRecord: function(issue) {
            issue.save().then(() => {
                this.transitionTo('issues');
            }, function() {
                console.log(arguments);
            });
        },

        deleteRecord: function(issue) {
            issue.destroyRecord().then(() => {
                this.transitionTo('issues');
            }, function(resp) {
                issue.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
