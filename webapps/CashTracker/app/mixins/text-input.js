import Em from 'ember';

export default Em.Mixin.create({
    selectOnClick: false,
    stopEnterKeyDownPropagation: false,

    click: function(event) {
        if (this.get('selectOnClick')) {
            event.currentTarget.select();
        }
    },

    keyDown: function(event) {
        if (this.get('stopEnterKeyDownPropagation') && event.keyCode === 13) {
            event.stopPropagation();

            return true;
        }
    }
});
