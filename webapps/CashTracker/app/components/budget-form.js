import Em from 'ember';

export default Em.Component.extend({
    budget: null,

    isEditMode: false,

    actions: {
        check: function() {
            var budget = this.get('budget');
            budget.then(function(m) {
                if (m.get('id') === 0 || m.get('name') === null) {
                    this.set('isEditMode', true);
                    this.transitionToRoute('budget');
                }
            }.bind(this));
        },

        edit: function() {
            this.set('isEditMode', true);
        },

        cancel: function() {
            let model = this.get('budget');
            if ((model.get('isNew') && model.get('isSaving') === false) ||
                (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
                model.rollbackAttributes();
            }
            this.set('isEditMode', false);
        },

        save: function() {
            var _this = this;
            var model = this.get('budget');
            model.save().then(function() {
                _this.set('isEditMode', false);
            });
        }
    }
});
