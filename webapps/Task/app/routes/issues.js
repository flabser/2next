import Em from 'ember';

export default Em.Route.extend({
    queryParams: {
        milestone: {
            refreshModel: true
        }
    },

    beforeModel: function(transition) {
        if (transition.targetName === 'issues.index') {
            if (!transition.queryParams.milestone || transition.queryParams.milestone === 'undefined') {
                transition.queryParams.milestone = '';
            }

            this.transitionTo('issues', {
                queryParams: transition.queryParams
            });
        }

        this._super(transition);
    },

    model: function(params) {
        return this.store.findAll('issue', params);
    },

    setupController: function(controller, model) {
        controller.set('tasks', this.store.findAll('task'));
        controller.set('issues', model);
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
