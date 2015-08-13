import Em from 'ember';

export default Em.Controller.extend({
    budget: Em.computed.alias('model'),

    isEditMode: false,

    actions: {
        check: function() {
            var budget = this.store.find('budget', 1);
            budget.then(function(m) {
                if (!m.get('name')) {
                    this.set('isEditMode', true);
                    this.transitionTo('budget');
                }
            }.bind(this));
        },

        edit: function() {
            this.set('isEditMode', true);
        },

        cancel: function() {
            this.set('isEditMode', false);
        },

        save: function() {
            var _this = this;
            var model = this.get('model');
            model.save().then(function() {
                _this.set('isEditMode', false);
            });
        }
    }
});
