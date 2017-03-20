$(function() {
    $('body').on('change', '.search-pagination input[type=radio]', function() {
         $(this).closest('form').submit();
    });
});
