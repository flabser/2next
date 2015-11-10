import Em from 'ember';

export default Em.Controller.extend({
    actions: {
        composeRecord: function() {
            this.transitionToRoute('issues.new');
        },

        saveIssue: function(issue) {
            issue.save().then(() => {
                this.transitionToRoute('issues.issue', issue);
            }, function() {
                console.log(arguments);
            });
        },

        deleteIssue: function(issue) {
            issue.destroyRecord().then(() => {
                this.transitionToRoute('issues');
            }, function(resp) {
                issue.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
