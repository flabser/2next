import Em from 'ember';

export default Em.Controller.extend({
    costCenter: Em.computed.alias('model'),

    actions: {
        save: function() {
            var _this = this;
            var model = this.get('costCenter');
            model.save().then(function() {
                _this.transitionTo('cost_centers');
            });
        }
    }
});
