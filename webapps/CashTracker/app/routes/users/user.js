import Em from 'ember';

export default Em.Route.extend({
    templateName: 'users/user',

    model: function(params) {
        return this.store.find('user', params.user_id);
    },

    deactivate: function() {
        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') == false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
    }
});
