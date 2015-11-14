import Em from 'ember';
import Validate from 'lof-task/utils/validator';

export default Em.Component.extend({
    classNames: ['tag-form'],
    classNameBindings: ['isInvalid:invalid', 'isEditing:edit'],
    // attributeBindings: ['draggable'],
    tag: null,

    // draggable: 'true',

    addChildTag: 'addChildTag',
    saveTag: 'saveTag',
    deleteTag: 'deleteTag',
    setTagActiveRoute: 'setTagActiveRoute',

    isEditing: Em.computed('tag.isNew', function() {
        return this.get('tag.isNew');
    }),

    isInvalid: Em.computed('tag.name', function() {
        return Validate.isEmpty(this.get('tag.name'));
    }),

    didInsertElement: function() {
        if (this.get('tag.isNew')) {
            this.$('input', this.element).focus();
        }
    },

    willDestroyElement: function() {
        if (this.get('tag.isNew')) {
            this.get('tag').destroyRecord();
        }
        this.set('isEditing', false);
    },

    actions: {
        addChildTag: function(tag) {
            this.sendAction('addChildTag', tag);
        },

        saveTag: function(tag) {
            this.sendAction('saveTag', tag, () => {
                this.set('isEditing', false);
            });
        },

        deleteTag: function(tag) {
            this.sendAction('deleteTag', tag);
            this.set('isEditing', false);
        },

        cancel: function(tag) {
            if (tag.get('isNew')) {
                tag.destroyRecord();
            } else {
                tag.rollbackAttributes();
                this.set('isEditing', false);
            }
        },

        setTagActiveRoute: function(tag) {
            this.sendAction('setTagActiveRoute', tag);
        },

        setEditing: function(tag) {
            if (!this.get('isEditing')) {
                this.send('toggleEditing', tag);
            }
            return false;
        },

        toggleEditing: function(tag) {
            this.toggleProperty('isEditing');
            this.send('setTagActiveRoute', tag);
            Em.run.schedule('afterRender', this, function() {
                this.$('input', this.element).focus();
            });
        }
    }
});
