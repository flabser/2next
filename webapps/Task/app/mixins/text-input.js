import Em from 'ember';

export default Em.Mixin.create({
    selectOnClick: false,
    stopEnterKeyDownPropagation: true,
    trim: false,

    click: function(event) {
        if (this.get('selectOnClick')) {
            event.currentTarget.select();
        }
    },

    keyDown: function(event) {
        if (this.get('stopEnterKeyDownPropagation') && event.keyCode === 13) {
            event.stopPropagation();
            event.preventDefault();

            return true;
        } else if (event.keyCode === 32 && this.get('type') === 'password') {
            return false;
        }
    }
});
