var nubis = {};

nubis.init = function() {

    $('#main-load').hide();
    $('#login-error').hide();

    // reg form
    var $regForm = $('form[name=signup]');
    if ($regForm.length) {
        $regForm.submit(function(e) {
            e.preventDefault();
            nubis.reg(this);
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

nubis.reg = function(form) {
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
        //url: 'Provider?client=' + screen.height + 'x' + screen.width,
        url: 'rest/page/reg-user',       
        data: $(form).serialize(),
        success: function(result) {
           console.log(result);
            var pr = result.split(',');
            if (pr.indexOf('email') != -1) {
                $('input[name=email]', form).addClass('invalid');
                $('.reg-email-invalid').css('height', 'auto');
            }
            if (pr.indexOf('user-exists') != -1) {
                $('.reg-email-exists', form).css('height', 'auto');
            }
            if (pr.indexOf('pwd-weak') != -1) {
                $('input[name=pwd]', form).addClass('invalid');
                $('.reg-pwd-weak', form).css('height', 'auto');
            }
            //
            var isReg = false;
            if (pr.indexOf('user-reg') != -1) {
                console.log('user-reg');
                isReg = true;
                $('input[name=pwd]', form).val('');
            }
            if (pr.indexOf('ok') != -1) {
                console.log('ok');
            }
            //

            if (pr.indexOf('verify-email-send') != -1) {
                var $msg = $('.reg-result-ok');
                $msg.css({
                    'display': 'block'
                });
                form.reset();

                setTimeout(function() {
                    $('.reg-result-ok').css({
                        'display': 'none'
                    });
                }, 1000 * 60);
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
                login: form.username.value,
                pwd: form.password.value
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

nubis.signOut = function(form) {
    $.ajax({
        method: 'DELETE',
        url: 'rest/session',
        success: function(result) {
            location.href = '?id=signin';
        }
    });
};

$(document).ready(nubis.init);
