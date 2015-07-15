import App from '../app';
import DS from 'ember-data';

App.ApplicationAdapter = DS.RESTAdapter.extend({

});

export default DS.RESTAdapter.extend({
    namespace: 'CashTracker/rest',

    pathForType: function(type) {
        switch (type) {
            case 'category':
                return 'categories';
            default:
                return type + 's';
        }
    }
});
