import Em from 'ember';

export default Em.Component.extend({
    value: null,
    multi: false,

    isExpense: false,
    isIncome: false,
    isTransfer: false,

    _types: [
        [
            'E', 'isExpense'
        ],
        [
            'I', 'isIncome'
        ],
        [
            'T', 'isTransfer'
        ]
    ],

    willInsertElement: function() {
        this._toggleState();
    },

    _toggleState: function() {
        var value = this.get('value');
        if (this.get('multi')) {
            this._types.forEach((t) => {
                if (value.contains(t[0])) {
                    this.set(t[1], true);
                } else {
                    this.set(t[1], false);
                }
            });
        } else {
            this._types.forEach((t) => {
                if (t[0] === value) {
                    this.set(t[1], true);
                } else {
                    this.set(t[1], false);
                }
            });
        }
    },

    actions: {
        toggle: function(type) {
            if (this.get('multi')) {
                var value = this.get('value');
                if (value.contains(type)) {
                    value.removeObject(type);
                } else {
                    value.addObject(type);
                }
            } else {
                this.set('value', type);
            }

            this._toggleState();
        }
    }
});
