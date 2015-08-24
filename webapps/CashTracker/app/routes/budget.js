import Em from 'ember';
import UnsavedModelRollbackMixin from '../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    model: function() {
        return this.store.find('budget', 1);
    },

    setEditModeFalse: function() {
        this.controller.send('cancel');
    }.on('deactivate')
});
