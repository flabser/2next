import Em from 'ember';
import TextInputMixin from '../mixins/text-input';

var Input = Em.TextField.extend(TextInputMixin, {
    classNames: 'input'
});

export default Input;
