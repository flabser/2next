import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    name: DS.attr('string'),
    date: DS.attr('date', {
        readOnly: true
    }),
    user: DS.belongsTo('user', {
        async: false,
        readOnly: true
    }),
    status: DS.attr('number', {
        readOnly: true
    }),

    validations: {
        name: {
            presence: true
        }
    }
});
