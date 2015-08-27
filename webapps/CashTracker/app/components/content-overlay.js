import Em from 'ember';

export default Em.Component.extend({
    tagName: 'div',

    classNames: ['content-overlay'],

    mouseDown: function(e) {
        e.preventDefault();
        this.send('hideOpenedNav');
    },

    touchStart: function(e) {
        e.preventDefault();
        this.send('hideOpenedNav');
    },

    actions: {
        hideOpenedNav() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});
