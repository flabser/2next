import Em from 'ember';

export default Em.Helper.helper(function(params) {
    var [obj, path] = params;

    return Ember.get(obj, path);
});
