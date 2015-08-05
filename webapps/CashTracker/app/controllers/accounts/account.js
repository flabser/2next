import Em from 'ember';

export default Em.Controller.extend({
    account: Em.computed.alias('model'),

    actions: {
        save: function() {
            let _this = this;
            let model = this.currentModel;
            model.save().then(function() {
                _this.transitionTo('accounts');
            });
        }
    }
});
