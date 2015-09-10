import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../mixins/model-form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelForm, {
    budget: null,
    isEditMode: false,

    actions: {
        check: function() {
            var budget = this.get('budget');
            budget.then((m) => {
                if (m.get('id') === 0 || m.get('name') === null) {
                    this.set('isEditMode', true);
                    this.transitionToRoute('budget');
                }
            });
        },

        edit: function() {
            this.set('isEditMode', true);
        },

        cancel: function() {
            let model = this.get('budget');
            if ((model.get('isNew') && model.get('isSaving') === false) ||
                (!model.get('isNew') && model.get('hasDirtyAttributes'))) {
                model.rollbackAttributes();
            }
            this.set('isEditMode', false);
        },

        save: function() {
            if (this.validate()) {
                this.get('budget').save().then(() => {
                    this.set('isEditMode', false);
                });
            }
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('budget.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
