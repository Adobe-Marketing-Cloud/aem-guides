$(function() {
    $('body').on('change', '.search-facets--group input[type=checkbox]', function() {
        $(this).closest('form').submit();
    });
});
