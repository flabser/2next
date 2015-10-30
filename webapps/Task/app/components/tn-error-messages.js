import Em from 'ember';

export default Em.Component.extend({
    tagName: 'div',
    classNames: ['error-messages'],

    errors: null,
    property: '',

    isVisible: Em.computed('errors', function() {
        var errors = this.get('errors'),
            property = this.get('property');
        return !Em.isEmpty(errors) && !Em.isEmpty(errors.get(property));
    }),

    messages: Em.computed('errors.[]', 'property', function() {
        var errors = this.get('errors'),
            property = this.get('property');

        if (!Em.isEmpty(errors) && errors.get(property)) {
            return errors.get(property);
        }
    })
});
