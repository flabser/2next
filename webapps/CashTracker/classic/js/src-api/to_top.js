;
$(function() {

	var isShow = false;
	var clickYOffset = 0;
	var click = false;
	var arrowUp = '▲';
	var arrowDown = '▼';

	var to_top = $('<div class="to_top"><div class="to_top-panel"><div class="to_top-button"></div></div></div>');

	$('body').append(to_top);

	var btn = $('.to_top-button', to_top);

	$('.to_top-panel', to_top).click(function() {
		click = true;

		if (!clickYOffset) {
			clickYOffset = window.pageYOffset;
			btn.html(arrowDown);
			$.scrollTo($('body'), 100, {
				axis : 'y'
			});
		} else {
			btn.html(arrowUp);
			$.scrollTo($('body'), 100, {
				axis : 'y',
				offset : clickYOffset
			});
			clickYOffset = 0;
		}
	});

	$(document).bind('mousewheel', function(event, delta) {
		if (click) {
			click = false;
			clickYOffset = 0;
			btn.html(arrowUp);

			if (window.pageYOffset == 0 && delta > 0) {
				show_or_hide();
			}
		} else {
			if (isShow && window.pageYOffset == 0) {
				to_top.hide();
				isShow = false;
				clickYOffset = 0;
			}
		}
	});

	$(window).scroll(show_or_hide);

	function show_or_hide() {
		if (click) {
			return;
		}

		if (window.pageYOffset > 300) {
			if (!isShow) {
				to_top.show();
				btn.html(arrowUp);
				isShow = true;
			}
		} else {
			if (isShow) {
				to_top.hide();
				btn.html(arrowDown);
				isShow = false;
			}
		}
	}

	show_or_hide();
});