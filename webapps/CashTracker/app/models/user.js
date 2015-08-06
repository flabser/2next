import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    email: DS.attr('string'),
    role: DS.attr('string'),

    validations: {
        login: {
            presence: true
        },
        email: {
            presence: true
        }
    }
});
