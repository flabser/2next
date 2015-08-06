import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    name: DS.attr('string'),

    validations: {
        name: {
            presence: true
        }
    }
});
