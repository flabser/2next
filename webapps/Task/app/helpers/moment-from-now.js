import Em from 'ember';
import moment from 'moment';

export default Em.Helper.helper(function(params) {
    let [date] = params;
    let md = moment(date);

    if (md.isValid()) {
        return md.fromNow();
    } else {
        return '';
    }
});
