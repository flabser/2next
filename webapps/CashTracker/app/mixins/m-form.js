import Em from 'ember';

export default Em.Mixin.create({
    form: Em.computed('parentView', function() {
        var parentView;
        parentView = this.get('parentView');
        while (parentView) {
            if (parentView.get('tagName') === 'form') {
                return parentView;
            }
            parentView = parentView.get('parentView');
        }
        return Em.assert(false, 'Cannot find form');
    }),

    model: Em.computed('form', 'form.model', function() {
        return this.get('form.model');
    })
});
