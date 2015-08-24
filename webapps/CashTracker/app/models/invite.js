import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    email: DS.attr('string'),
    message: DS.attr('string'),

    validations: {
        email: {
            presence: true
        },
        message: {
            presence: true
        }
    }
});
