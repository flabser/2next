import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'accounts/account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    users: function() {
        return this.store.findAll('user');
    }.property(),

    setupController: function(controller, model) {
        controller.set('account', model);
        controller.set('users', this.get('users'));
    },

    actions: {
        save: function() {
            var _this = this;
            var model = this.get('controller').get('account');
            model.save().then(function() {
                _this.transitionTo('accounts');
            });
        }
    }
});
