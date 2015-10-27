/**
 * Smooth scroll to anchor
 */

$(function() {
    $('a[href*=#]:not([href=#])').click(
        function() {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: (target.offset().top) // 50px navbar height
                    }, 500);
                    return false;
                }
            }
        });
});

/**
 * Contact form
 */

$(document).ready(function(e) {
    $('form[name=contact_us]').submit(function(e) {
        var $form = $(this);
        $.ajax({
            url: 'rest/page/sendmail',
            type: 'POST',
            data: $form.serialize(),
            dataType: 'json',
            beforeSend: function(xhr) {
                $form.fadeTo('slow', 0.33);
                $('button', $form).attr('disabled', true);
                $('.has-error', $form).removeClass('has-error');
                $('.help-block', $form).html('');
                $('#form_message').removeClass('alert-success').html('');
            },
            success: function(response, status) {
                var hasErrors = response._Page.elements[0].name === 'errors';
                if (hasErrors) {
                    var errors = response._Page.elements[0].value;
                    // Error messages
                    if (errors.indexOf('email') != -1) {
                        $('input[name=email]', $form).parent().addClass('has-error');
                        $('input[name=email]', $form).next('.help-block').html('error.email');
                    }
                    if (errors.indexOf('subject') != -1) {
                        $('input[name=subject]', $form).parent().addClass('has-error');
                        $('input[name=subject]', $form).next('.help-block').html('error.subject');
                    }
                    if (errors.indexOf('message') != -1) {
                        $('textarea[name=message]', $form).parent().addClass('has-error');
                        $('textarea[name=message]', $form).next('.help-block').html('error.message');
                    }
                    if (errors.indexOf('recaptcha') != -1) {
                        $('.form-captcha .help-block').addClass('has-error');
                        $('.form-captcha .help-block').html('error.recaptcha');
                    }
                }
                //
                if (response._Page.elements[0].name === 'result') {
                    $('#form_message').addClass('alert-success').html(response._Page.elements[0].value);
                    $form[0].reset();
                    setTimeout(function() {
                        $('#form_message').removeClass('alert-success').html('');
                    }, 5000);
                }
            },
            complete: function(xhr, status) {
                $form.fadeTo('fast', 1);
                $('button', $form).removeAttr('disabled');
                // Refresh Captcha
                grecaptcha.reset();
            }
        });

        return false;
    });
});
