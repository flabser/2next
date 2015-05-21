;
nbApp.inViewEdit = function() {
	var _ed = nbApp.inViewEdit;
	var $addNewNode = $('[data-action=add_new]');
	var $layoutContent = $('.layout_content');

	_ed.contentInit = function() {
		$('[data-action=save]').on('click', function(e) {
			e.preventDefault();
		})

		$('[data-action=close]').on('click', function(e) {
			$addNewNode.removeClass('hidden');
			window.history.back(-1);
			e.preventDefault();
		});

		$('.entry-link').on('click', function(e) {
			_ed.edit(this.href);
			e.preventDefault();
		});
	};

	_ed.edit = function(url) {
		//
		if (url != window.location) {
			window.history.pushState(null, null, url);
		}
		//
		$addNewNode.addClass('hidden');

		$('#main-load').show();
		$.get(url).then(function(r) {
			$layoutContent.html(r);
			$('#main-load').hide();
			_ed.contentInit();
		});
	};

	$addNewNode.on('click', function(e) {
		_ed.edit(this.href);
		e.preventDefault();
	});

	$('a', '.side').on('click', function(e) {
		_ed.edit(this.href);
		e.preventDefault();
	});

	$(window).bind('popstate', function(e) {
		_ed.edit(location.href);
		e.preventDefault();
	});
};

$(function() {
	// nbApp.inViewEdit();
});
