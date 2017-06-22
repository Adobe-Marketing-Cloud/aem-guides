(function (site, $) {
    'use strict';
    var wkndnavbar = $(site + " .navbar"),
        scroll;
    
    if($(window).scrollTop() > 0) {
         wkndnavbar.addClass("navbar-sticky");
    }
    $(window).scroll(function(){
         
         scroll = $(window).scrollTop();
    if(scroll > 10) {
        wkndnavbar.addClass("navbar-sticky");
    } else {
        wkndnavbar.removeClass("navbar-sticky");
    }
});
}('.wknd-site',jQuery));