import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('category');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('categories.new');
        },

        saveCategory: function(category) {
            var _this = this;
            category.save().then(function() {
                _this.transitionTo('categories');
            });
        },

        deleteRecord: function(category) {
            var _this = this;
            category.destroyRecord().then(function() {
                _this.transitionTo('categories');
            }, function(resp) {
                category.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
