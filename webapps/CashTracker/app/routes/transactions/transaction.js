import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    actions: {
        save: function() {
            var _this = this;
            var model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('transactions');
            });
        }
    }
});
