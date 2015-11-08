var nubis = {};

nubis.init = function() {

    $('#main-load').hide();
    $('#login-error').hide();
    $('[data-toggle="popover"]').popover();

    $('.js-app-edit').click(function(e) {
        $('.tn-app.edit').removeClass('edit');
        $(this).parent('.tn-app').addClass('edit');
    });

    $('.js-app-close-edit').click(function(e) {
        $('.tn-app.edit').removeClass('edit');
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
        var appDesc = $(this).parent('.tn-tpl').find('.tn-tpl-description').data('content');
        $('[name=apptype]', '#form-app').val(appType);
        $('[name=appname]', '#form-app').val('');
        $('[name=description]', '#form-app').val(appDesc);
        $('[name=description]', '#form-app').one('focus', function() {
            $(this).select();
        });
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
        dataType: 'json',
        url: 'rest/session/signup',
        data: $(form).serialize(),
        success: function(result) {
            var map = result.outcome.messages;
            var alert;
            if (result.outcome.type == 'ERROR') {            	 
            	 alert = '#red-alert';
            } else if (result.outcome.type == 'WARNING') {
            	alert = '#yellow-alert';
            } else {
            	alert = '#green-alert';
            }
            $(alert).text(map[Object.keys(map)[0]]).show();
            setTimeout(function() {
                $(alert).hide();
            }, 5000);
        },
        error: function(err) {
        	  $('#main-load').hide();
        	  var obj = $.parseJSON(err.responseText);
        	  var map = obj.outcome.messages;
              $('#red-alert').text(map[Object.keys(map)[0]]).show();
              setTimeout(function() {
                  $('#red-alert').hide();
              }, 5000);
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
            location.href = '?id=ws';           
        },
        error: function(err) {
            $('#main-load').hide();
            var obj = $.parseJSON(err.responseText);
            $('#red-alert').text(obj.authUser.error).show();
            
            setTimeout(function() {
                $('#red-alert').hide();
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
        url: 'rest/application/regapp',
        data: $(form).serialize(),
        success: function(resp) {
            var appReg = false;
            if (resp.outcome.type == 'OK') {
                appReg = true;
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
