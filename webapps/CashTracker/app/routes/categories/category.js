import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    actions: {
        save: function() {
            let _this = this;
            let model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('categories');
            });
        }
    }
});
