$(function() {
    //Facet filter
    $('body').on('change', '.search-facets input[type=checkbox]', function() {
        $(this).closest('form').submit();
    });

    //toggle collapse facets
    $('.search-facet-toggle').click(function(event) {
        event.preventDefault();
        $(this).blur();
        $(".search-facets").toggleClass("open");
        $(".search-facets--content").toggle('fast');
     });
});
