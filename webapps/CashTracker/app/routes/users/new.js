import UserRoute from './user';

export default UserRoute.extend({
    model: function() {
        return this.store.createRecord('user', {

        });
    },

    deactivate: function() {
        var model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') == false) {
            model.rollbackAttributes();
        }
    }
});
