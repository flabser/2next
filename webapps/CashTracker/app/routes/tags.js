import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('tag');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('tags.new');
        },

        deleteRecord: function(tag) {
            var _this = this;
            tag.destroyRecord().then(function() {
                _this.transitionTo('tags');
            }, function(resp) {
                tag.rollback();
                tag.reload();
                alert(resp.errors.message);
            });
        }
    }
});
