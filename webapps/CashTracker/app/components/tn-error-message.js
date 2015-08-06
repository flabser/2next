import Em from 'ember';
import ModelFormMixin from '../mixins/m-form';
import ModelFormPropertyMixin from '../mixins/m-form-property';

export default Em.Component.extend(ModelFormMixin, ModelFormPropertyMixin, {
    tagName: 'div',
    classNames: ['error-message'],

    isVisible: Em.computed('status', function() {
        return this.get('status') === 'error';
    }),

    message: Em.computed('errors', function() {
        return this.get('errors.firstObject.message') || this.get('errors.firstObject') || this.get('text');
    }),

    init: function() {
        return this._super();
    }
});
