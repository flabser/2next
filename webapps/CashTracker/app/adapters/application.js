import DS from 'ember-data';

function getHost() {
    return window.location.pathname.replace('index.html', '');
}

export default DS.RESTAdapter.extend({
    namespace: getHost() + 'rest',

    pathForType: function(type) {
        switch (type) {
            case 'category':
                return 'categories';
            case 'budget':
                return 'budget';
            default:
                return type + 's';
        }
    },

    shouldReloadAll: function() {
        return false;
    },

    headers: {
        'API_KEY': 'secret key'
    },

    ajax: function(url, type, options) {
        return this._super(url, type, options);
    }
});
