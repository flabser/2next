import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'accounts/account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    }
});
