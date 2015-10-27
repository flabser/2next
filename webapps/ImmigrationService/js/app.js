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
    $('fieldset').removeAttr('disabled');

    $('form[name=contact_us]').submit(function(e) {
        var $form = $(this);
        $.ajax({
            url: 'rest/service/sendmail',
            type: 'POST',
            data: $form.serialize(),
            dataType: 'json',
            beforeSend: function(xhr) {
                $('fieldset', $form).attr('disabled', true);
                $('.has-error', $form).removeClass('has-error');
                $('.help-block', $form).html('');
                $('#form_message').removeClass('alert-success').html('');
            },
            success: function(response, status) {
                $('#form_message').addClass('alert-success').html('success');
                $form[0].reset();
                setTimeout(function() {
                    $('#form_message').removeClass('alert-success').html('');
                }, 5000);
            },
            error: function(xhr) {
                var outcome = xhr.responseJSON.outcome;

                // Error messages
                if (outcome.message.indexOf('email_incorrect') != -1) {
                    $('input[name=email]', $form).parent().addClass('has-error');
                    $('input[name=email]', $form).next('.help-block').html('error.email');
                }
                if (outcome.message.indexOf('subject_incorrect') != -1) {
                    $('input[name=subject]', $form).parent().addClass('has-error');
                    $('input[name=subject]', $form).next('.help-block').html('error.subject');
                }
                if (outcome.message.indexOf('message_incorrect') != -1) {
                    $('textarea[name=message]', $form).parent().addClass('has-error');
                    $('textarea[name=message]', $form).next('.help-block').html('error.message');
                }
                if (outcome.message.indexOf('captcha_incorrect') != -1) {
                    $('.form-captcha .help-block').addClass('has-error');
                    $('.form-captcha .help-block').html('error.recaptcha');
                }

                return xhr;
            },
            complete: function(xhr, status) {
                $('fieldset', $form).removeAttr('disabled');
                // Refresh Captcha
                grecaptcha.reset();
            }
        });

        return false;
    });
});
