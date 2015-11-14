import Em from 'ember';
import moment from 'moment';

export default Em.Helper.helper(function(params) {
    let [date] = params;

    return moment(date).fromNow();
});
