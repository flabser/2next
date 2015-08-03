import DS from 'ember-data';

function getHost() {
    return window.location.pathname;
}

export default DS.RESTAdapter.extend({
    namespace: getHost() + 'rest',

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
