import Ember from 'ember';

export default Ember.ArrayController.extend({
    queryParams: ['type', 'offset', 'limit', 'order_by']
});
