import Em from 'ember';

export default Em.Component.extend({
    tagName: '',
    deleteIssue: 'deleteIssue',

    actions: {
        deleteIssue: function(issue) {
            this.sendAction('deleteIssue', issue);
        }
    }
});
