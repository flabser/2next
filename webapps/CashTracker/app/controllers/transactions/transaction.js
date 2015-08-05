import Em from 'ember';

export default Em.Controller.extend({
    transaction: Em.computed.alias('model'),

    actions: {
        save: function() {
            var _this = this;
            var model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('transactions');
            });
        }
    }
});
