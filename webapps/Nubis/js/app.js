var nubis = {};

nubis.init = function() {

	$('#main-load').hide();
	$('#login-error').hide();

	$('.js-app-edit').click(function(e) {
		$('.tn-app').removeClass('edit');
		$(this).parent('.tn-app').addClass('edit');
	});

	$('.js-app-close-edit').click(function(e) {
		$('.tn-app').removeClass('edit');
	});

	$('.tn-app').click(function(e) {
		e.stopPropagation();
	});

	$('body').click(function() {
		$('.tn-app.edit').removeClass('edit');
	});

	$('.js-app-remove').click(function() {
		nubis.removeApp($(this).data('app-id'));
	});

	$('.js-app-create').click(function(e) {
		$('#app_сreate_modal').modal({
			show: true,
			backdrop: 'static',
			keyboard: false
		});

		var appType = $(this).data('app-type');
		$('[name=apptype]', '#form-app').val(appType);
		$('[name=appname]', '#form-app').val('');
		$('[name=description]', '#form-app').val('');
	});

	// reg form
	var $regForm = $('form[name=signup]');
	if ($regForm.length) {
		$regForm.submit(function(e) {
			e.preventDefault();
			nubis.signUp(this);
		});

		$('input', $regForm).blur(function() {
			if ($(this).attr('required')) {
				if ($(this).val()) {
					$(this).removeClass('invalid');
				}
			}

			$('.reg-result-ok').css({
				'display': 'none'
			});
		});

		$('input', $regForm).focus(function() {
			$(this).removeClass('invalid');
			$('.reg-email-invalid,.reg-email-exists,.reg-pwd-weak').css('height', '0px');
		});
	}

	// login form
	$loginForm = $('form[name=signin]');
	if ($loginForm.length) {
		$loginForm.submit(function(e) {
			e.preventDefault();
			nubis.login(this);
		});
	}
};

nubis.signUp = function(form) {
	if ($(form).hasClass('process')) {
		return false;
	}

	$('#main-load').show();
	$('input', form).removeClass('invalid');
	$('.reg-email-invalid,.reg-email-exists,.reg-pwd-weak', form).css('height', '0px');
	$(form).addClass('process');

	$.ajax({
		method: 'POST',
		datatype: 'text',
		// url: 'Provider?client=' + screen.height + 'x' + screen.width,
		url: 'rest/session/signup',
		data: $(form).serialize(),
		success: function(result) {			
			console.log(result.outcome);
			
			if (result.outcome.type == 'ERROR') {
				var errorId = result.outcomе.errorId;
				if (errorId == 'EMAIL_IS_INCORRECT') {
					$('input[name=email]', form).addClass('invalid');
					$('.reg-email-invalid').css('height', 'auto');
					alert(result.outcome.message[0]);
				}else if (errorId == 'USER_EXISTS') {
					$('.reg-email-exists', form).css('height', 'auto');
					alert(result.outcome.message[0]);
				}else if (errorId == 'WEAK_PASSWORD') {
					$('input[name=pwd]', form).addClass('invalid');
					$('.reg-pwd-weak', form).css('height', 'auto');
				}
			}else if (result.outcome.type == 'WARNING') {
				if (result.outcomе.warningId == 'VERIFY_EMAIL_SENDING_ERROR') {
					alert(result.outcome.message[0]);
				}
			}else{
				var isReg = false;
				//if (pr.indexOf('user-reg') != -1) {
					console.log('user-reg');
					isReg = true;
					$('input[name=pwd]', form).val('');
				//}
				//if (pr.indexOf('ok') != -1) {
					console.log('ok');
				//}
			//

				if (pr == 'verify email sending error') {
					var $msg = $('.reg-result-ok');
					$msg.css({
						'display': 'block'
					});
					form.reset();

					setTimeout(function() {
						$('.reg-result-ok').css({'display': 'none'});
					}, 1000 * 60);
				}
			}
		},
		error: function(err) {
			console.log(err);
		},
		complete: function() {
			$(form).removeClass('process');
			$('#main-load').hide();
		}
	});
};

nubis.login = function(form) {
	if ($(form).hasClass('process')) {
		return false;
	}

	$('#login-error').hide();
	$('#main-load').show();
	$(form).addClass('process');

	$.ajax({
		method: 'POST',
		dataType: 'json',
		contentType: 'application/json',
		url: 'rest/session',
		data: JSON.stringify({
			"authUser": {
				login: form.login.value,
				pwd: form.pwd.value
			}
		}),
		success: function(result) {
			console.log(result);
			if (result.authUser.status === 'PASSWORD_INCORRECT') {
				$('#main-load').hide();
				$('#login-error').show();

				setTimeout(function() {
					$('#login-error').hide();
				}, 5000);
			} else {
				location.href = '?id=ws';
			}
		},
		error: function(err) {
			$('#main-load').hide();
			$('#login-error').show();

			setTimeout(function() {
				$('#login-error').hide();
			}, 5000);
		},
		complete: function() {
			$(form).removeClass('process');
		}
	});

	return false;
};

nubis.logOut = function(form) {
	$.ajax({
		method: 'DELETE',
		url: 'rest/session',
		success: function(result) {
			location.href = '?id=login';
		}
	});
};

nubis.removeApp = function(appId) {
	$.ajax({
		method: 'DELETE',
		url: '?id=unreg_app&app=' + appId,
		success: function(result) {
			location.reload();
		}
	});
};

nubis.createApp = function(form) {
	$('#app_сreate_modal button').prop('disabled', true);
	$('#app_сreate_modal button.btn-app-submit').button('loading');

	$.ajax({
		method: 'POST',
		url: 'rest/page/reg-app',
		data: $(form).serialize(),
		success: function(resp) {
			var appReg = false;
			for (var i = 0; i < resp._Page.elements.length; i++) {
				if ('application-registered' === resp._Page.elements[i].value) {
					appReg = true;
				}
			}

			if (appReg) {
				$('#app_сreate_modal').modal('hide');
				location.reload();
			}
		},
		error: function(err) {
			console.log(err);
		},
		complete: function() {
			$('#app_сreate_modal button').prop('disabled', false);
		}
	});
};

$(document).ready(nubis.init);
