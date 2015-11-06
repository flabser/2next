import Em from 'ember';

export default Em.Controller.extend({
    queryParams: ['at', 's', 'tags'],

    actions: {
        addIssue: function(issue) {
            this.transitionToRoute('issues.new', issue);
        },

        saveIssue: function(issue, callback) {
            issue.save().then(() => {
                this.transitionToRoute('issues.issue', issue);
                callback && callback();
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
