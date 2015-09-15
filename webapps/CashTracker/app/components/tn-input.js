import Ember from 'ember';
import TextInputMixin from '../mixins/text-input';

var Input = Ember.TextField.extend(TextInputMixin, {
    classNames: 'input'
});

export default Input;
