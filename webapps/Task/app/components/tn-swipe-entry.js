import Em from 'ember';
import swipeEntryUtil from 'lof-task/utils/swipe-ventry';

export default Em.Component.extend({
    tagName: 'div',

    touchStart: function(e) {
        swipeEntryUtil.touchStart(e);
    },

    touchMove: function(e) {
        swipeEntryUtil.touchMove(e);
    },

    touchEnd: function(e) {
        swipeEntryUtil.touchEnd(e);
    }
});
