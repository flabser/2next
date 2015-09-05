import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'transactions/transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    setupController: function(controller, model) {
        controller.set('transaction', model);
    }
});
