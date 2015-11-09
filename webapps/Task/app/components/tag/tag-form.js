import Em from 'ember';
import Validate from 'lof-task/utils/validator';

export default Em.Component.extend({
    classNames: ['tag-form'],
    classNameBindings: ['isInvalid:invalid', 'isEditing:edit'],
    tag: null,

    addChildTag: 'addChildTag',
    saveTag: 'saveTag',
    deleteTag: 'deleteTag',
    cancel: 'cancel',
    setTagActiveRoute: 'setTagActiveRoute',

    isEditing: Em.computed('tag.isNew', function() {
        return this.get('tag.isNew');
    }),

    isInvalid: Em.computed('tag.name', function() {
        return Validate.isEmpty(this.get('tag.name'));
    }),

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
            console.log('in');
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

        toggleEditing: function(tag) {
            this.toggleProperty('isEditing');
            this.send('setTagActiveRoute', tag);
        }
    }
});
