import Em from 'ember';
import UnsavedModelRollbackMixin from '../../mixins/m-unsaved-model-rollback';

export default Em.Route.extend(UnsavedModelRollbackMixin, {
    templateName: 'tags/tag',

    model: function(params) {
        return this.store.find('tag', params.tag_id);
    },

    setupController: function(controller, model) {
        controller.set('tag', model);
    }
});
