import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('category');
    },

    deactivate: function() {
        this._super();
        this.store.unloadAll('category');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('categories.new');
        },

        saveCategory: function(category) {
            category.save().then(() => {
                this.transitionTo('categories');
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
