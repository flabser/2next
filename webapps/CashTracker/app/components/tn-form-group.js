import Em from 'ember';
import ModelFormMixin from '../mixins/m-form';
import ModelFormPropertyMixin from '../mixins/m-form-property';

export default Em.Component.extend(ModelFormMixin, ModelFormPropertyMixin, {
    tagName: 'div',
    classNames: ['form-group', 'control-group'],
    classNameBindings: ['status'],

    init: function() {
        return this._super();
    }
});
