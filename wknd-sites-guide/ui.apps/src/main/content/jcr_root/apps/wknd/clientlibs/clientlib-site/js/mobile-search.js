/*
    javascript for mobile navbar. Scoped for 
    
    <body class="wknd-site">
        <nav class="navbar">
            ...
*/

(function (site, $) {
    'use strict';
    var searchbtn = $(site + " .cmp-search-mobile--btn"),
        navbarbrand = $(site + " .navbar-brand"),
        searchdiv = $( site + " .cmp-search-mobile"),
        searchinput = $(site + " .cmp-search-mobile--input"),
        closetogglebtn = $( site + " .cmp-mobile--toggle-close"),
        navbartogglebtn = $( site + " .cmp-mobile--toggle-nav");
    
    /* Search Icon clicked -> open up mobile search */
    $(searchbtn).click(function(e) {
        e.preventDefault();
        $(navbarbrand).hide();
        $(searchdiv).addClass("open");
        $(navbartogglebtn).hide();
        $(closetogglebtn).show();
        $(searchinput).show().focus();
     
     });
    
    /* Close Search Icon clicked -> close mobile search and reset */
    $(closetogglebtn).click(function(e) {
        e.preventDefault();
        $(closetogglebtn).hide();
        $(searchinput).hide();
         $(searchinput).val("");
        $(searchdiv).removeClass("open");
        $(navbarbrand).fadeIn();
        $(navbartogglebtn).fadeIn();
    });
    
    /* Navbar toggle -> open and close navigation */
    $(navbartogglebtn).click(function(e) {
        var navtarget = $(this).data('target');
        
        e.preventDefault();
        if($(this).hasClass('open')) {
            $(this).removeClass('open');
            $(navbarbrand).removeClass('open');
            $('i', this).removeClass('wkndicon-ico-cross');
            $('i', this).addClass('wkndicon-ico-bm');
            $(navtarget).removeClass('open');
        } else {
            $(this).addClass('open');
            $(navbarbrand).addClass('open');
            $('i', this).removeClass('wkndicon-ico-bm');
            $('i', this).addClass('wkndicon-ico-cross');
            $(navtarget).addClass('open');
        }
        
        
    });
    
}('.wknd-site .navbar',jQuery));