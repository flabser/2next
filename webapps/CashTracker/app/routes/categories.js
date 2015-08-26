import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('category');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('categories.new');
        },

        deleteRecord: function(category) {
            var _this = this;
            category.destroyRecord().then(function() {
                _this.transitionTo('categories');
            }, function(resp) {
                category.rollback();
                alert(resp.errors.message);
            });
        }
    }
});
