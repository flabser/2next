import Em from "ember";

export default Em.Helper.helper(function(params) {
    let type = params[0],
        el = document.createElement('span');

    switch (type) {
        case 'I':
            el.innerHTML = '<i class="fa fa-caret-up green"></i>';
            break;
        case 'E':
            el.innerHTML = '<i class="fa fa-caret-down red"></i>';
            break;
        case 'T':
            el.innerHTML = '<i class="fa fa-retweet blue"></i>';
            break;
    }

    return Em.String.htmlSafe(el.outerHTML);
});
