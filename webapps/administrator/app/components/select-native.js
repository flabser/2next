AdminApp.SelectNativeComponent = Ember.Component.extend({
    content: null,
    value: null,

    didInsertElement: function() {
        var v = this.get('value');
        this.$('option[value=' + v + ']').attr('selected', true);
    },

    actions: {
        change: function() {
            var selectEl = this.$('select')[0],
                selectedIndex = selectEl.selectedIndex,
                content = this.get('content'),
                selection = content.objectAt(selectedIndex);

            this.set('value', selection.value);
        }
    }
});
