import Em from 'ember';

export default Em.Component.extend({
    tagName: 'div',
    classNames: ['select-transaction-type'],
    value: null,

    types: [{
        code: 'E',
        name: 'expense'
    }, {
        code: 'I',
        name: 'income'
    }, {
        code: 'T',
        name: 'transfer'
    }],

    _showActive: function() {
        var type = this.get('value');
        if (type) {
            var el = Em.$('[data-type=' + type + ']:not(.active)', this.element);
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
        toggle: function(type) {
            if (type !== this.get('value')) {
                this.set('value', type);
            } else {
                this.set('value', '');
            }
        }
    }
});
