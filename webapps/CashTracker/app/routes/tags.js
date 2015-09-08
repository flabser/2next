import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('tag');
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
