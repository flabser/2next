import Em from 'ember';

export default Em.Controller.extend({
    account: Em.computed.alias('model'),

    users: function() {
        return this.store.findAll('user');
    }.property(),

    actions: {
        save: function() {
            let _this = this;
            let model = this.get('account');
            model.save().then(function() {
                _this.transitionTo('accounts');
            });
        }
    }
});
