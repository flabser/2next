nbApp.saveUserProfile = function() {

	if ($('#newpwd').val() != $('#newpwd2').val()) {
		nb.dialog.warn({
			message : 'Введенные пароли не совпадают'
		});
		return;
	}

	nbApp.xhrSaveUserProfile($('form')[0]).then(function(xml) {
		var redir = $(xml).find('redirect').text();
		if (redir === '') {
			redir = $(xml).find('history').find('entry[type=page]:last').text();
		}
		$.cookie('lang', $('select[name=lang]').val(), {
			path : '/',
			expires : 30
		});
		$.cookie('refresh', $('select[name=refresh]').val(), {
			path : '/',
			expires : 30
		});
		$.cookie('skin', $('select[name=skin]').val(), {
			expires : 30
		});
		if (redir === '') {
			window.history.back();
		} else {
			window.location = redir;
		}
	}, function(xhr, ajaxOptions, thrownError) {
		if (xhr.status === 400) {
			if (xhr.responseText.indexOf('Old password has not match') != -1) {
				nb.dialog.warn({
					message : 'Некорректно заполнено поле [пароль по умолчанию]'
				});
			} else {
				nb.dialog.error({
					message : xhr.responseText
				});
			}
		}
	});
};
