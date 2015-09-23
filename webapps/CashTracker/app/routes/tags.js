import Em from 'ember';
import InfinityRoute from "ember-infinity/mixins/route";

export default Em.Route.extend(InfinityRoute, {
    perPageParam: "limit", // instead of "per_page"
    pageParam: "page", // instead of "page"
    totalPagesParam: "meta.total", // instead of "meta.total_pages"

    model: function() {
        // return this.store.findAll('tag');
        return this.infinityModel("tag", {
            perPage: 20,
            startingPage: 1
        });
    },

    deactivate: function() {
        this._super();
        this.store.unloadAll('tag');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('tags.new');
        },

        saveTag: function(tag) {
            tag.save().then(() => {
                this.transitionTo('tags');
            });
        },

        deleteRecord: function(tag) {
            tag.destroyRecord().then(() => {
                this.transitionTo('tags');
            }, function(resp) {
                tag.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
