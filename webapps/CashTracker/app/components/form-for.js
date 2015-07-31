import Em from 'ember';

export default Em.Component.extend({
    tagName: 'form',

    method: 'POST',

    classNames: ['form-wrapper'],

    attributeBindings: ['method'],

    action: 'save',

    validationErrors: [],

    /*isNotValid: Em.computed('model.isValid', function() {
        if (!Em.isNone(this.get('model.isValid'))) {
            return !this.get('model.isValid');
        } else {
            return false;
        }
    }),*/

    submit: function(e) {
        if (e) {
            e.preventDefault();
        }

        if (Em.isNone(this.get('model.validate'))) {
            return this.send(this.get('action'));
        } else {
            var promise = this.get('model').validate();
            return promise.then(function() {
                return function() {
                    if (this.get('model.isValid')) {
                        return this.send(this.get('action'));
                    }
                }.bind(this);
            }.bind(this), function(err) {
                console.log('form for err', err, this.get('errors'));
            }).catch(function(err) {
                console.log('form-for catch err', err);
                return false;
            });
        }
    }
});
