import Em from 'ember';

export default Em.Route.extend({
    queryParams: {
        at: {
            refreshModel: true
        },
        st: {
            refreshModel: true
        },
        tags: {
            refreshModel: true
        },
        u: {
            refreshModel: true
        }
    },

    beforeModel: function(transition) {
        console.log('issues beforeModel, get check changes');
    },

    model: function(params) {
        return this.store.query('issue', params);
    },

    setupController: function(controller, model) {
        controller.set('issues', model);
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
