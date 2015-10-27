/* scrollSpyNavBar */

$(function() {
    var prevYOffset = 0,
        hideYOffset = 0,
        scrollTopDiff = 120,
        isHidden = false;
    var $bar = $('.navbar');
    var navBarHeight = $bar.height();

    $(window).scroll(scrollSpyBar);
    scrollSpyBar();

    function scrollSpyBar() {
        if (window.pageYOffset > prevYOffset) {
            if (!isHidden) {
                $bar.addClass('navbar-scroll-hide');
                $('.navbar .open .dropdown-toggle').dropdown('toggle');
                $('.navbar .navbar-collapse').collapse('hide');
                isHidden = true;
            }
            hideYOffset = window.pageYOffset;
        } else {
            if (isHidden && (window.pageYOffset < navBarHeight || (hideYOffset - window.pageYOffset > scrollTopDiff))) {
                $bar.removeClass('navbar-scroll-hide');
                isHidden = false;
            }
        }
        prevYOffset = window.pageYOffset;
    }
});

/* Auto close navbar on click */

$('.navbar-brand, .navbar-nav > li > a').click(function() {
    if (!$(this).hasClass('dropdown-toggle')) {
        $('.navbar-collapse').collapse('hide');
    }
});

/* Change navbar class on scroll */

$('.wrapper').waypoint(function(wp) {
    $('.navbar').toggleClass('js-navbar-top');
    $('.navbar.js-toggle-class').toggleClass('navbar-default navbar-inverse');
    return false;
}, {
    offset: '-20px'
});

/* Change navbar class on collapse/uncollapse in its top position */

$('.navbar .navbar-collapse').on('show.bs.collapse', function() {
    $('.navbar.js-navbar-top').toggleClass('navbar-default navbar-inverse');
    $('.navbar').toggleClass('js-toggle-class');
});

$('.navbar .navbar-collapse').on('hide.bs.collapse', function() {
    $('.navbar.js-navbar-top').toggleClass('navbar-default navbar-inverse');
    $('.navbar').toggleClass('js-toggle-class');
});

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
            url: 'rest/service/sendmail',
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
            error: function(err) {
            	 console.log(err.outcome);
                if (err.outcome.type == 'ERROR') {
                	
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
