import Em from 'ember';

export default Em.Controller.extend({
    budget: Em.computed.alias('model'),

    isEditMode: false,

    actions: {
        edit: function() {
            this.set('isEditMode', true);
        },

        cancel: function() {
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
