function saveUserProfile(redirecturl) {
	if ($("#newpwd").val() != $("#newpwd2").val()) {
		nbApp.ui.notify("Введенные пароли не совпадают");
		return;
	}

	$.ajax({
		type : "POST",
		url : "Provider?type=save&element=user_profile",
		datatype : "html",
		data : $("#frm").serialize(),
		success : function(xml) {
			var redir = $(xml).find('redirect').text();
			if (redir == '') {
				redir = $(xml).find('history').find("entry[type=page]:last").text();
			}
			$.cookie("lang", $("select[name='lang']").val(), {
				path : "/",
				expires : 30
			});
			$.cookie("refresh", $("select[name='refresh']").val(), {
				path : "/",
				expires : 30
			});
			$.cookie("skin", $("select[name='skin']").val(), {
				expires : 30
			});
			if (redir == '') {
				window.history.back();
			} else {
				window.location = redir;
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			if (xhr.status == 400) {
				if (xhr.responseText.indexOf("Old password has not match") != -1) {
					nbApp.ui.notify("Некорректно заполнено поле 'пароль по умолчанию'");
				} else {
					nbApp.ui.notify({
						msg : "Error",
						type : "error"
					});
				}
			}
		}
	});
}
