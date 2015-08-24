import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'users/user',

    model: function(params) {
        return this.store.find('user', params.user_id);
    }
});
