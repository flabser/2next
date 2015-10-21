import Em from 'ember';

let Checkbox = Em.Object.extend({
    isSelected: Em.computed('value', 'selection', {
        get() {
            return this.get('selection').contains(this.get('value'));
        },

        set(_, checked) {
            let selected = this.get('selection').contains(this.get('value'));

            if (checked && !selected) {
                this.get('selection').addObject(this.get('value'));
            } else if (!checked && selected) {
                this.get('selection').removeObject(this.get('value'));
            }

            return checked;
        }
    })
});

export default Em.Component.extend({
    classNames: ['multiselect-checkboxes'],

    tagName: 'ul',

    options: Em.A(),

    selection: Em.A(),

    labelProperty: null,

    valueProperty: null,

    disabled: false,

    checkboxes: Em.computed('options', 'labelProperty', 'valueProperty', 'selection', function() {
        let labelProperty = this.get('labelProperty');
        let valueProperty = this.get('valueProperty');
        let selection = this.get('selection') || Em.A();

        let checkboxes = this.get('options').map((option) => {
            let label, value;

            if (labelProperty) {
                if (typeof option.get === 'function') {
                    label = option.get(labelProperty);
                } else {
                    label = option[labelProperty];
                }
            } else {
                label = String(option);
            }

            if (valueProperty) {
                if (typeof option.get === 'function') {
                    value = option.get(valueProperty);
                } else {
                    value = option[valueProperty] || option.value;
                }
            } else {
                value = option;
            }

            return Checkbox.create({
                label: label,
                value: value,
                selection: selection
            });
        });

        return Em.A(checkboxes);
    })
});
