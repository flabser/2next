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
        }
    },

    model: function(params) {
        return this.store.query('issue', params);
    },

    setupController: function(controller, model) {
        controller.set('issues', model);
    }
});
