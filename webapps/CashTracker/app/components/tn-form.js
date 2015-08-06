import Em from 'ember';

export default Em.Component.extend({
    tagName: 'form',
    method: 'POST',
    novalidate: 'novalidate',
    classNames: ['form-wrapper'],
    attributeBindings: ['method'],

    model: null,

    action: 'save',

    submit: function(e) {
        if (e) {
            e.preventDefault();
        }

        if (Em.isNone(this.get('model.validate'))) {
            return this.get('targetObject').send(this.get('action'));
        } else {
            var promise = this.get('model').validate();
            return promise.then(function() {
                if (this.get('model.isValid')) {
                    return this.get('targetObject').send(this.get('action'));
                }
            }.bind(this));
        }
    }
});
