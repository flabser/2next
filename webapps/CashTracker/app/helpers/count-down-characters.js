import Em from 'ember';

export default Em.Helper.helper(function(params) {
    var [content, maxChar] = params;
    var el, length;

    if (!content || !maxChar) {
        return;
    }

    length = content.length;
    if (length === 0 || maxChar <= length) {
        return;
    }

    el = document.createElement('div');
    el.className = 'char-left text-muted';

    /*if (length > maxChar) {
        el.style.color = '#E25440';
    } else {
        el.style.color = '#9FBB58';
    }*/

    el.innerHTML = maxChar - length;

    return Em.String.htmlSafe(el.outerHTML);
});
