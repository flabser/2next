import IssueRoute from './issue';

export default IssueRoute.extend({
    model: function(params, transition) {
        var milestoneDate = new Date();
        /*if (transition.queryParams) {
            if (transition.queryParams.at === 'today') {

            } else if (transition.queryParams.at === 'week') {

            } else if (transition.queryParams.at === 'inbox') {

            }
        }*/

        return this.store.createRecord('issue', {
            milestone: milestoneDate
        });
    }
});
