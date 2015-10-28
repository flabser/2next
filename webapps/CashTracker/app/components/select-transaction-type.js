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

    didInsertElement: function() {
        var type = this.get('value');
        if (type) {
            this.send('toggle', type);
        }
    },

    actions: {
        toggle: function(type) {
            var el = Em.$('[data-type=' + type + ']:not(.active)', this.element);

            if (el.length) {
                Em.$('.active', this.element).removeClass('active');
                el.addClass('active');
                this.set('value', type);
            }
        }
    }
});
