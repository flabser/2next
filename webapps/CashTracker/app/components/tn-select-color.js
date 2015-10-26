import Em from 'ember';

export default Em.Component.extend({
    tagName: 'ul',
    classNames: ['select-color'],
    value: null,

    colors: [
        '#F44336', '#9C27B0', '#673AB7', '#3F51B5', '#2196F3', '#009688', '#4CAF50', '#8BC34A', '#FFC107', '#795548', '#607D8B'
    ],

    didInsertElement: function() {
        var color = this.get('value');
        if (color) {
            Em.$('[data-color=' + color + ']:not(.active)', this.element).addClass('active');
        } else {
            this.set('value', '');
        }
    },

    actions: {
        setColor: function(colorValue) {
            var el = Em.$('[data-color=' + colorValue + ']:not(.active)', this.element);
            Em.$('.active', this.element).removeClass('active');

            if (el.length) {
                el.addClass('active');
                this.set('value', colorValue);
            } else {
                this.set('value', '');
            }
        }
    }
});
