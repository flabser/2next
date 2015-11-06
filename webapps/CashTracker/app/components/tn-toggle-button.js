import Em from 'ember';

export default Em.Component.extend({
    tagName: 'div',
    classNames: ['tn-toggle-button'],
    multi: false,
    value: null,
    values: null,

    _showActive: function() {
        let value = this.get('value');
        if (value) {
            let el = Em.$('[data-type=' + value + ']:not(.active)', this.element);
            if (el.length) {
                Em.$('.active', this.element).removeClass('active');
                el.addClass('active');
            }
        } else {
            Em.$('.active', this.element).removeClass('active');
        }
    },

    valueObserver: Em.observer('value', function() {
        this._showActive();
    }),

    didInsertElement: function() {
        this._showActive();
    },

    actions: {
        toggle: function(value) {
            if (value !== this.get('value')) {
                this.set('value', value);
            } else {
                this.set('value', '');
            }
        }
    }
});
