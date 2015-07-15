import Ember from 'ember';

export default Ember.ArrayController.extend({
    queryParams: ['offset', 'limit', 'order_by']
});
