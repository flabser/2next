import DS from 'ember-data';

export default DS.RESTAdapter.extend({
    namespace: 'CashTracker/rest',

    pathForType: function(type) {
        switch (type) {
            case 'category':
                return 'categories';
            default:
                return type + 's';
        }
    },

    shouldReloadAll: function() {
        return true;
    }
});
