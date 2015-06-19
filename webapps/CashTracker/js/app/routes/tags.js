CT.TagsRoute = Ember.Route.extend({

    templateName: 'tags',

    queryParams: {
        offset: {
            refreshModel: true
        },
        limit: {
            refreshModel: true
        }
    },

    model: function(params) {
        return this.store.find('tag');
    },

    beforeModel: function(transition) {
        if (transition.targetName === 'tags.index') {
            if (!parseInt(transition.queryParams.limit, 0)) {
                transition.queryParams.limit = 10;
            }

            if (!parseInt(transition.queryParams.offset, 0)) {
                transition.queryParams.offset = 0;
            }

            this.transitionTo('tags', {
                queryParams: transition.queryParams
            });
        }
    }
});
