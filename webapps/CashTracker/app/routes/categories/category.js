import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    actions: {
        save: function(model) {
            model.save().then(function() {
                model.transitionTo('categories');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
