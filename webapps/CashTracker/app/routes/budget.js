import Em from 'ember';
import UnsavedModelRollbackMixin from '../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    model: function() {
        return this.store.find('budget', 1);
    },

    setupController: function(controller, model) {
        controller.set('budget', model);
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('transactions.new');
        }
    }
});
