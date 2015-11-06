AdminApp.SelectUserStatusComponent = Ember.Component.extend({
    tagName: 'div',
    classNames: ['select-user-staus'],
    value: null,

    types: [{
        code: '455',
        name: 'NOT_VERIFIED'
    }, {
        code: '456',
        name: 'REGISTERED'
    }, {
        code: '457',
        name: 'USER_WAS_DELETED'
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
