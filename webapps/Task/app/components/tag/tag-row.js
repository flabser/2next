import Em from 'ember';

export default Em.Component.extend({
    classNames: ['tag-row'],
    tag: null,

    addChildTag: 'addChildTag',
    saveTag: 'saveTag',
    deleteTag: 'deleteTag',
    setTagActiveRoute: 'setTagActiveRoute',

    actions: {
        addChildTag: function(tag) {
            this.sendAction('addChildTag', tag);
        },

        saveTag: function(tag, callSuccess, callError) {
            this.sendAction('saveTag', tag, callSuccess, callError);
        },

        deleteTag: function(tag) {
            this.sendAction('deleteTag', tag);
        },

        setTagActiveRoute: function(tag) {
            this.sendAction('setTagActiveRoute', tag);
        }
    }
});
