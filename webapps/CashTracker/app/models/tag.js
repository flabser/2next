import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    name: DS.attr('string'),
    color: DS.attr('number'),

    validations: {
        name: {
            presence: true
        },
        color: {
            numericality: true,
            inclusion: {
                range: [0, 10],
                allowBlank: true
            }
        }
    }
});
