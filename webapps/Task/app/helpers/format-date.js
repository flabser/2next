import Em from 'ember';
import moment from 'moment';

export default Em.Helper.helper(function(params) {
    let [date, format] = params;

    if (!format) {
        format = 'DD.MM.YYYY';
    }

    let md = moment(date);

    if (md.isValid()) {
        return md.format(format);
    } else {
        return '';
    }
});
