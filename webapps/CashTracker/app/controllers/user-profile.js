import Em from 'ember';

export default Em.Controller.extend({
    user: Em.computed.alias('model')
});
