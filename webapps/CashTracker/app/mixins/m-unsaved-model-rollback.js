import Em from 'ember';

export default Em.Mixin.create({
    deactivate: function() {
        this._super();

        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') === false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
    }
});
