nbApp.initUI = function() {

	$('.js-toggle-nav-app').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavApp();
	});

	$('.js-toggle-nav-ws').mousedown(function(e) {
		e.preventDefault();
		nbApp.uiToggleNavWorkspace();
	});

	if ($('.js-content-overlay')) {
		$('.js-content-overlay').mousedown(function(e) {
			e.preventDefault();
			nbApp.uiHideOpenedNav();
		});

		$('.js-content-overlay')[0].addEventListener('touchstart', function(e) {
			e.preventDefault();
			nbApp.uiHideOpenedNav();
		}, false);
	}

	$('[data-role="side-tree-toggle"]').click(function() {
		$(this).parent().toggleClass('side-tree-collapse');
	});

	$('[data-role="toggle"]').click(function() {
		$(this).parent().toggleClass($(this).data('toggle-class'));
	});

	// action
	$('.action_save_user_profile').click(function() {
		nbApp.saveUserProfile();
	});

	$('.action_save_and_close').click(function() {
		nbApp.saveDoc(frm.last_page.value);
	});

	$('.action_delete_document').click(function(e) {
		e.preventDefault();
		if ($(e.target).hasClass('disabled') === false) {
			nbApp.delSelectedDocument();
		}
		return false;
	});

	$('[name=docid]:checkbox').bind('change', function(e) {
		nbApp.uiToggleAvailableActions(e);
	});

	$('[data-toggle=docid]:checkbox').bind('click', function(e) {
		nbApp.uiToggleAvailableActions(e);
	});

	nbApp.attachInitFileUpload('#fileupload');

	//
	if (typeof $.fn.tabs !== 'undefined') {
		$('#tabs').tabs();
	}
};
