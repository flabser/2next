import Em from 'ember';

export default Em.Route.extend({
    /*queryParams: {
        at: {
            refreshModel: true
        },
        st: {
            refreshModel: true
        },
        tags: {
            refreshModel: true
        }
    },*/

 /*   beforeModel: function(transition) {
        if (transition.targetName === 'issues.index') {
            if (!transition.queryParams.at || transition.queryParams.at === 'undefined') {
                transition.queryParams.at = '';
            }
            if (!transition.queryParams.st || transition.queryParams.st === 'undefined') {
                transition.queryParams.st = '';
            }
            if (!transition.queryParams.tags || transition.queryParams.tags === 'undefined') {
                transition.queryParams.tags = '';
            }

            this.transitionTo('issues', {
                queryParams: transition.queryParams
            });
        }

        this._super(transition);
    },*/

    /*model: function(params) {
        return this.store.query('issue', params);
    },*/

    setupController: function(controller, model) {
        // controller.set('issues', model);
        controller.set('tags', this.store.findAll('tag'));
        controller.set('users', this.store.findAll('user'));
    }
});
