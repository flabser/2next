import Em from 'ember';

export default Em.Mixin.create({
    saving: false,

    deactivate: function() {
        this._super();

        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') === false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
    },

    actions: {
        willTransition: function(transition) {
            if (this.get('saving') === true) {
                // transition.abort();
            }
        }
    }
});
