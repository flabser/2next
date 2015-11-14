import Em from 'ember';

export default Em.Helper.helper(function(params) {
    let [type] = params,
    el = document.createElement('span');

    switch (type) {
        case 'DRAFT':
            el.innerHTML = '<i class="issue-status-draft fa fa-edit"></i>';
            break;
        case 'PROCESS':
            el.innerHTML = '<i class="issue-status-process fa fa-spinner"></i>';
            break;
        case 'CLOSE':
            el.innerHTML = '<i class="issue-status-close fa fa-check-square-o"></i>';
            break;
    }

    return Em.String.htmlSafe(el.outerHTML);
});
