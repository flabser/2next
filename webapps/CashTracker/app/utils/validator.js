import Em from 'ember';

function isEmpty(str) {
    return Em.isEmpty(str); // (!str || 0 === str.length || !str.trim());
}

function validateEmail(email) {
    var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    return re.test(email);
}

function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function isDate(dateStr) {
    var d = moment(dateStr);
    if (d == null || !d.isValid()) {
        return false;
    }
    return true;
}

function notEmptyAndEqual(v1, v2) {
    if (isEmpty(v1) || isEmpty(v2)) {
        return false;
    }

    return v1 === v2;
}

var Validate = {
    isEmpty: isEmpty,
    isEmail: validateEmail,
    isNumeric: isNumeric,
    isDate: isDate,
    notEmptyAndEqual: notEmptyAndEqual
};

export default Validate;
