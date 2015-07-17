import Em from 'ember';

export default Em.Component.extend({
    tagName: 'form',

    method: 'POST',

    classNames: ['form-wrapper', 'form-component'],

    attributeBindings: ['method'],

    validationErrors: [],

    submit: function(e) {
        if (e) {
            e.preventDefault();
        }

        var _this = this;
        var model = Em.get(this, 'model');
        var promise = model.validate();
        return promise.then(function() {
                if (model.get('isValid')) {
                    return this.sendAction('save');
                }
            }.bind(this), function(err) {
                console.log(err);
            })
            .catch(function(err) {
                console.log('form-for catch err', model.errors);
                return false;
            });
    }
});
