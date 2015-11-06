import Em from 'ember';
import TextInputMixin from 'lof-task/mixins/text-input';

var Input = Em.TextField.extend(TextInputMixin, {
    classNames: 'input'
});

export default Input;
