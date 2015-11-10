import IssueRoute from './issue';

export default IssueRoute.extend({
    model: function(params) {
        return this.store.find('issue', params.issue_id);
    }
});
