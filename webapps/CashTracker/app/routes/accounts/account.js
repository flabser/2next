import Ember from 'ember';

export default Ember.Route.extend({
    templateName: 'accounts/account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    actions: {
        save: function() {
            let _this = this;
            let model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('accounts');
            });
        }
    }
});
