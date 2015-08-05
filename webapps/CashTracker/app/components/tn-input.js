import Em from 'ember';
import ModelFormPropertyMixin from '../mixins/m-form-property';

export default Em.Component.extend(ModelFormPropertyMixin, {
    label: void 0,
    placeholder: void 0,
    required: void 0,
    autofocus: void 0,
    disabled: void 0,

    init: function() {
        this._super();

        var propertyIsModel = this.get('parentView.propertyIsModel');
        if (propertyIsModel) {
            return Em.Binding.from("model" + '.' + (this.get('propertyName')) + '.content').to('selection').connect(this);
        } else {
            return Em.Binding.from("model" + '.' + (this.get('propertyName'))).to('value').connect(this);
        }
    },

    hasValue: Em.computed('value', function() {
        return this.get('value') !== null;
    }).readOnly(),

    focusOut: function() {
        console.log('focus out', this);
        return this.set('canShowErrors', true);
    }
});
