import Em from 'ember';

export default Em.Route.extend({
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    deactivate: function() {
        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') == false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
    }
});
