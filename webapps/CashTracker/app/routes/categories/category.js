import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    }
});
