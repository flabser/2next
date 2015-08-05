import Em from 'ember';

export default Em.Mixin.create({
    property: void 0,

    propertyName: Em.computed('parentView.property', function() {
        if (this.get('property')) {
            return this.get('property');
        } else if (this.get('parentView.property')) {
            return this.get('parentView.property');
        } else {
            return Em.assert(false, 'Property could not be found.');
        }
    }),

    status: Em.computed('errors.length', function() {
        if (this.get('errors.length')) {
            return 'error';
        } else {
            return 'success';
        }
    }),

    init: function() {
        this._super();
        return Em.Binding.from('model.errors.' + this.get('propertyName')).to('errors').connect(this);
    }
});
