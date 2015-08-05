import Em from 'ember';

export default Em.Controller.extend({
    category: Em.computed.alias('model'),

    actions: {
        save: function() {
            let _this = this;
            let model = this.get('category');
            model.save().then(function() {
                _this.transitionTo('categories');
            });
        }
    }
});
