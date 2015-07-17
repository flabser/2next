import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'cost-center',

    model: function(params) {
        return this.store.find('cost_center', params.costcenter_id);
    },

    actions: {
        save: function(model) {
            model.save().then(function() {
                model.transitionTo('cost_centers');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
