import DS from 'ember-data';

function getHost() {
    return window.location.pathname.replace('index.html', '');
}

export default DS.RESTAdapter.extend({
    namespace: getHost() + 'rest',

    pathForType: function(type) {
        return type + 's';
    },

    shouldReloadAll: () => false
});
