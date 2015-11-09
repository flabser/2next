import Em from 'ember';

export default Em.Controller.extend({
    actions: {
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
