import Em from 'ember';

export default Em.Mixin.create({
    saving: false,

    deactivate: function() {
        let model = this.currentModel;
        if (model.get('isNew') && model.get('isSaving') === false) {
            model.deleteRecord();
        } else if (!model.get('isNew') && model.get('hasDirtyAttributes')) {
            model.rollbackAttributes();
        }

        return true;
    },

    actions: {
        willTransition: function(transition) {
            if (this.get('saving') === true) {
                // transition.abort();
            }

            return true;
        }
    }
});
