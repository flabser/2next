import Em from 'ember';

export default Em.Controller.extend({
    user: Em.computed.alias('model'),

    actions: {
        sendInvite: function() {
            var _this = this;
            var model = this.get('user');
            model.save().then(function() {
                _this.transitionToRoute('users');
            });
        }
    }
});
