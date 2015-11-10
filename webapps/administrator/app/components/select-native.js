AdminApp.SelectNativeComponent = Ember.Component.extend({
    tagName: 'select',
    content: null,
    value: null,

    didInsertElement: function() {
        var selectEl = this.get('element');
        var v = this.get('value');
        this.$('option[value=' + v + ']', selectEl).attr('selected', true);
    },

    change: function() {
        this.send('change');
    },

    actions: {
        change: function() {
            var selectEl = this.get('element'),
                selectedIndex = selectEl.selectedIndex,
                content = this.get('content'),
                selection = content.objectAt(selectedIndex);

            this.set('value', selection.value);
        }
    }
});
