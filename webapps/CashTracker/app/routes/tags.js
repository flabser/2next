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
            var _this = this;
            tag.save().then(function() {
                _this.transitionTo('tags');
            });
        },

        deleteRecord: function(tag) {
            var _this = this;
            tag.destroyRecord().then(function() {
                _this.transitionTo('tags');
            }, function(resp) {
                tag.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
