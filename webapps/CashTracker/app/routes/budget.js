import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        var _this = this;
        var b = this.store.find('budget', 1);
        return b.then(function(m) {
            if (m.get('name') == null) {
            	_this.controllerFor('budget').set('isEditMode', true);
                return _this.store.createRecord('budget');
            } else {
                return b;
            }
        });
    },

    deactivate: function() {
        let model = this.currentModel;
        if ((model.get('isNew') && model.get('isSaving') == false) ||
            (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
            model.rollbackAttributes();
        }
    },

    actions: {
        add() {
            this.transitionTo('transactions.new');
        }
    }
});
