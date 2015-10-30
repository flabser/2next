import Em from 'ember';

export default Em.Helper.helper(function(params) {
    var [lhs, rhs] = params;

    return lhs === rhs;
});
