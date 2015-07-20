import TagRoute from './tag';

export default TagRoute.extend({
    model: function(params) {
        return this.store.createRecord('tag', {
            name: '',
            color: 0
        });
    },

    deactivate: function() {
        var model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    }
});
