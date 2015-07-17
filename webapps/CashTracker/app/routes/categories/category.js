import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    actions: {
        save: function() {
            var _this = this;
            var model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('categories');
            });
        }
    }
});
