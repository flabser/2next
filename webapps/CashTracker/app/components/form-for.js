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
        console.log('1', this);
        var model = Em.get(this, 'model');
        return model.validate()
            .then(function() {
                console.log(this.get('action'));
                // this.sendAction('save', model);
                model.save().then(function() {
                    console.log('2', this);
                }.bind(this));
            }.bind(this))
            .catch(function(err) {
                console.log('form-for catch err', err);
                return false;
                // generate error messages on this.validationErrors
            });
    },

    actions: {
        save: function() {
            console.log('form for save');
        }
    }
});
