import Em from 'ember';

export default Em.Mixin.create({
    unsavedModelRollback: function() {
        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') === false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
    }.on('deactivate')
});
