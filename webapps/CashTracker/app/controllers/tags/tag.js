import Em from 'ember';

export default Em.Controller.extend({
    tag: Em.computed.alias('model'),

    actions: {
        save: function() {
            var _this = this;
            var model = this.get('tag');
            model.save().then(function() {
                _this.transitionToRoute('tags');
            });
        }
    }
});
