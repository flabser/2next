import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('category');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('categories.new');
        },

        saveRecord: function(category) {
            category.save().then(() => {
                this.transitionTo('categories');
            }, function(resp) {
                console.log(arguments);
            });
        },

        deleteRecord: function(category) {
            category.destroyRecord().then(() => {
                this.transitionTo('categories');
            }, function(resp) {
                category.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
