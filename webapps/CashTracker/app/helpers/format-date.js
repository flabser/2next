import Em from "ember";

export default Em.Helper.helper(function(params) {
    let [date, format] = params;

    if (!format) {
        format = 'MM.DD.YYYY';
    }

    return moment(date).format(format);
});
