import Em from 'ember';

export default Em.Controller.extend({
    queryParams: ['type', 'offset', 'limit', 'order_by']
});
