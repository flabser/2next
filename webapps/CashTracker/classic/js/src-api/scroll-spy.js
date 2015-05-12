nbApp.initScrollSpySide = function() {

	var offsetTop = 0;
	var sideOnTop = false;
	var $side = []; // $('.nav-app .side');

	if ($side.length) {
		offsetTop = $('.layout_header')[0].clientHeight;

		$(window).scroll(scrollSpySide);
		scrollSpySide();
	}

	function scrollSpySide() {
		if (window.pageYOffset > offsetTop) {
			if (!sideOnTop) {
				$side.css('margin-top', '0px');
				sideOnTop = true;
			}
		} else {
			if (sideOnTop) {
				$side.css('margin-top', '');
				sideOnTop = false;
			}
		}
	}
};

nbApp.initScrollSpyActionBar = function() {

	var offsetTop = 0;
	var isFixed = false;
	var $bar = $('.action-bar-top');

	if ($bar.length && $bar.find('.pagination, .btn').length) {
		var $parent = $bar.parent();
		$parent.css('min-height', $parent.height() + 'px');
		offsetTop = $bar[0].clientHeight + $parent.height();
		$bar.css('width', $bar[0].clientWidth);

		$(window).scroll(scrollSpyActionBar);
		scrollSpyActionBar();
	}

	function scrollSpyActionBar() {
		if (window.pageYOffset > offsetTop) {
			if (!isFixed) {
				$bar.addClass('action-bar-fixed');
				isFixed = true;
			}
		} else {
			if (isFixed) {
				$bar.removeClass('action-bar-fixed');
				isFixed = false;
			}
		}
	}
};
