// Sticking w jQuery since it is easy for everyone to understand!
$(function() {

    function show($el, flag) {
        if (flag) {
            $el.show();
        } else {
            $el.hide();
        }
    }

    $('body').on('keyup', '.search form[data-quick-suggestions] input[name=q]', function() {
        var $form = $(this).closest('form'),
            $lists = $form.find('[data-quick-lists]'),
            $list = $form.find('[data-quick-suggestions]');

        if($form.data('quick-suggestions-enabled') === true) {

        $.get($form.data('quick-suggestions'), $form.serialize(), function(data) {
            $list.find('dd').remove();

            $.each(data.suggestions, function(index, suggestion) {
                $list.append($('<dd>').text(suggestion));
            });

            show($list, data.suggestions.length > 0);
            show($lists, $lists.find('dd').length > 0);
        });
        }
    });


    $('body').on('keyup', '.search form[data-quick-search-results] input[name=q]', function() {
        var $form = $(this).closest('form'),
            $lists = $form.find('[data-quick-lists]'),
            $list = $form.find('[data-quick-results]');

        if($form.data('quick-search-results-enabled') === true) {
        $.get($form.data('quick-search-results'), $form.serialize(), function(data) {
            $list.find('dd').remove();

            $.each(data.results, function(index, result) {
                $list.append($('<dd>').append($('<a>').attr('href', result.url).text(result.title)));
            });

            show($list, data.results.length > 0);
            show($lists, $lists.find('dd').length > 0);
        });
        }
    });

    $('.search-field').on('click', '.quick-lists .quick-suggestions dd', function() {
        var $form = $(this).closest('form'),
            $searchField = $form.find('input[name=q]'),
            autoComplete = $(this).text();

        $searchField.val(autoComplete);
        $form.submit();
	});


});