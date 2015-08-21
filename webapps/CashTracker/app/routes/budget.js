import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.find('budget', 1);
    },

    deactivate: function() {
        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') == false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
        this.controller.send('cancel');
    }
});
