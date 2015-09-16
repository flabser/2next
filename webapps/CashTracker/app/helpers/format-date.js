import Em from "ember";

export default Em.Helper.helper(function(params) {
    let date = params[0],
        format = params[1] || 'MM.DD.YYYY';

    return moment(date).format(format);
});
