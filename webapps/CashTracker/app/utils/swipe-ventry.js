import Em from 'ember';

const {
    $
} = Em;

var touch = {};
var t = 150;
var i = 80;
var n = 200;
var s = 70;
var a = 10;
var o = 10;
var r = true;
var l = "js-swipe";

touch.touchStart = function(touchEvent) {
    if (1 === touchEvent.originalEvent.touches.length) {
        touch.tapSwipedMessage = Boolean(touch.swipedMsg);
        touch.hideSwipedMsg();
        touch.resetValue(touchEvent);
    }
};

touch.touchMove = function(touchEvent) {
    var changedTouch = touchEvent.originalEvent.changedTouches[0];
    if (touch.isValidTouch(touchEvent)) {
        touch.delta = touch.x - changedTouch.pageX;
        touch.defineUserAction(changedTouch);
        if (touch.startSwipe) {
            if (!touch.startSwipeTriggered) {
                touch.startSwipeTriggered = true;
            }
            r && touch.move(touchEvent.currentTarget);
            touchEvent.preventDefault();
        }
    }
};

touch.touchEnd = function(touchEvent) {
    if (touch.isValidTouch(touchEvent, true) && touch.startSwipe) {
        touch.swipedMsg = touchEvent.currentTarget;
        if ((touch.delta > i) || (new Date() - touch.startTime < n)) {
            // qu.animations.setStyle(touch.swipedMsg, -s, 0, t);

            touch.swipedMsg.style.cssText = '';

            var parent = $(touch.swipedMsg).parent('.entry-wrap');
            parent.addClass('entry-action-open');

            touch.swipedMsg.classList.add(l);
        } else {
            touch.hideSwipedMsg();
        }
        touchEvent.stopPropagation();
        touchEvent.preventDefault();
    }
};

touch.hideSwipedMsg = function() {
    if (touch.swipedMsg) {
        var i = touch.swipedMsg;
        setTimeout(function() {
            i.classList.remove(l);
            i = null;
        }, t);
        // qu.animations.setStyle(touch.swipedMsg, 0, 0, t);

        touch.swipedMsg.style.cssText = '';

        var parent = $(touch.swipedMsg).parent('.entry-wrap');
        parent.removeClass('entry-action-open');

        touch.swipedMsg = null;
    }
};

touch.resetValue = function(t) {
    var i = t.originalEvent.changedTouches[0];
    touch.touchId = i.identifier;
    touch.startTime = new Date();
    touch.startSwipe = !1;
    touch.startScroll = !1;
    touch.delta = 0;
    touch.x = i.pageX;
    touch.y = i.pageY;
    touch.startSwipeTriggered = !1;
};

touch.bindEvents = function(t, i) {
    t["touchstart " + i] = touch.touchStart;
    t["touchend " + i] = touch.touchEnd;
    t["touchmove " + i] = touch.touchMove;
};

touch.defineUserAction = function(t) {
    Math.abs(touch.y - t.pageY) > o && !touch.startSwipe ? touch.startScroll = !0 : touch.delta > a && !touch.startScroll && (touch.startSwipe = !0);
};

touch.isValidTouch = function(t, i) {
    var n = i ? "changedTouches" : "targetTouches";
    return t.originalEvent[n][0].identifier === touch.touchId;
};

touch.move = function(el) {
    var i = Math.min(-touch.delta, 0); - s > i && (i = -s + (s + i) / 8);
    // qu.animations.setStyle(el, i, 0, 0)

    el.style.cssText = 'transition:transform 0ms ease-in-out;-moz-transform:translate3d(' + i + 'px,0,0);-webkit-transform:translate3d(' + i + 'px,0,0);transform:translate3d(' + i + 'px,0,0)';
};

export default touch;
