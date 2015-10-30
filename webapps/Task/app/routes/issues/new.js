import IssueRoute from './issue';

export default IssueRoute.extend({
    model: function() {
        return this.store.createRecord('issue');
    }
});
