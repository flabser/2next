nbApp.wlc = {};

nbApp.wlc.init = function() {

	var $regForm = $('form[name=form-reg]');
	$regForm.submit(function(e) {
		e.preventDefault();
		nbApp.wlc.reg(this);
	});
	$('input', $regForm).blur(function() {
		if ($(this).attr('required')) {
			if ($(this).val()) {
				$(this).removeClass('invalid');
			}
		}
	});
	$('input', $regForm).focus(function() {
		$(this).removeClass('invalid');
		$(".reg-email-invalid,.reg-email-exists,.reg-pwd-weak").css("height", "0px");
	});
	//
	$('.js-ShowLoginForm').click(nbApp.wlc.loginFormOpen);

/*	$('.js-content-overlay')[0].addEventListener('touchstart', function(e) {
		e.preventDefault();
		nbApp.wlc.loginFormClose();
	}, false);

	$('.js-content-overlay').mousedown(function(e) {
		e.preventDefault();
		nbApp.wlc.loginFormClose();
	});*/

	if (location.hash === "#sign-in") {
		nbApp.wlc.loginFormOpen();
	}
};

nbApp.wlc.loginFormOpen = function() {
	$("body").addClass("login-form-open");
};

nbApp.wlc.loginFormClose = function() {
	$("body").removeClass("login-form-open");
};

nbApp.wlc.login = function(form) {
	form.submit();
};

nbApp.wlc.setLang = function(lang) {
	$.cookie('lang', lang);
	window.location.reload();
};

nbApp.wlc.reg = function(form) {
	if ($(form).hasClass("process")) {
		return false;
	}

	$('input', form).removeClass('invalid');
	$(".reg-email-invalid,.reg-email-exists,.reg-pwd-weak", form).css("height", "0px");
	$(form).addClass("process");

	nb.ajax({
		method : 'POST',
		datatype : 'text',
		url : 'Provider?client=' + screen.height + 'x' + screen.width,
		data : $(form).serialize(),
		success : function(result) {
			var pr = result.split(",");
			if (pr.indexOf("email") != -1) {
				$("input[name=email]", form).addClass("invalid");
				$(".reg-email-invalid").css("height", "auto");
			}
			if (pr.indexOf("user-exists") != -1) {
				$(".reg-email-exists", form).css("height", "auto");
			}
			if (pr.indexOf("pwd-weak") != -1) {
				$("input[name=pwd]", form).addClass("invalid");
				$(".reg-pwd-weak", form).css("height", "auto");
			}
			//
			var isReg = false;
			if (pr.indexOf("user-reg") != -1) {
				console.log("user-reg");
				isReg = true;
			}
			if (pr.indexOf("ok") != -1) {
				console.log("ok");
			}
			//
			if (false && isReg) {
				var $loginForm = $('form[name=login-form]');
				$("[name=login]", $loginForm).val($("input[name=email]", form).val());
				$("[name=pwd]", $loginForm).val($("input[name=pwd]", form).val());
				nbApp.wlc.login($loginForm[0]);
			}

			if (pr.indexOf("verify-email-send") != -1) {
				nb.utils.notify({
					type : "info",
					message : "Для завершения регистрации подтвердите свой email"
				}).show();
			}
		},
		error : function(err) {
			console.log(err);
		},
		complete : function() {
			$(form).removeClass("process");
		}
	});
};
