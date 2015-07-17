import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    actions: {
        save: function(model) {
            model.save().then(function() {
                model.transitionTo('transactions');
            }, function(err) {
                console.log(err);
            });
        }
    }
});
