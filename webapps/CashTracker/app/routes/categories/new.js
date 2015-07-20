import CategoryRoute from './category';

export default CategoryRoute.extend({
    model: function(params) {
        return this.store.createRecord('category', {

        });
    },

    deactivate: function() {
        let model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    }
});
