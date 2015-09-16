import Em from 'ember';

export default Em.Helper.helper(function(arr /* hashParams */ ) {
    var el,
        content,
        maxCharacters,
        length;

    if (!arr || arr.length < 2) {
        return;
    }

    content = arr[0] || '';
    maxCharacters = arr[1];
    length = content.length;

    if (length === 0 || maxCharacters <= length) {
        return;
    }

    el = document.createElement('div');
    el.className = 'char-left text-muted';

    /*if (length > maxCharacters) {
        el.style.color = '#E25440';
    } else {
        el.style.color = '#9FBB58';
    }*/

    el.innerHTML = maxCharacters - length;

    return Em.String.htmlSafe(el.outerHTML);
});
