import Em from 'ember';

export default Em.Controller.extend({
    queryParams: {
        at: {
            refreshModel: true
        },
        st: {
            refreshModel: true
        },
        tags: {
            refreshModel: true
        }
    }
});
