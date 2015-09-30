var promoApp = {
    toggleNavbar: function(wp) {
        var $nav = $(".navbar");
        if (wp === 'down') {
            $nav.removeClass("js-navbar-top");
            $nav.removeClass("navbar-default").addClass("navbar-inverse");
        } else if (wp === 'up') {
            if (!$nav.hasClass('js-show')) {
                $nav.addClass("js-navbar-top");
                $nav.addClass("navbar-default").removeClass("navbar-inverse");
            }
        }
    }
};

/**
 * Navigation
 */

/* Auto close navbar on click */

$(".navbar-nav > li > a").click(function() {
    if (!$(this).hasClass('dropdown-toggle')) {
        $(".navbar-collapse").collapse('hide');
    }
});

/* Change navbar class on scroll */

$(".wrapper").waypoint(function(wp) {
    promoApp.toggleNavbar(wp);
    return false;
}, {
    offset: "-20px"
});

/* Change navbar class on collapse/uncollapse in its top position */

$('.wrapper .navbar-collapse').on('show.bs.collapse', function() {
    $(".navbar.js-navbar-top").toggleClass("navbar-default navbar-inverse");
    $(".navbar").toggleClass("js-show");
});

$('.wrapper .navbar-collapse').on('hide.bs.collapse', function() {
    $(".navbar.js-navbar-top").toggleClass("navbar-default navbar-inverse");
    $(".navbar").toggleClass("js-show");
    if (window.pageYOffset < 20) {
        promoApp.toggleNavbar('up');
    }
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
                        scrollTop: (target.offset().top - 50) // 50px offset for navbar menu
                    }, 500);
                    return false;
                }
            }
        });
});
