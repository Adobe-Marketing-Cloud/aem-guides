// Sticking w jQuery since it is easy for everyone to understand!
$(function() {

    function show($el, flag) {
        if (flag) {
            $el.show();
        } else {
            $el.hide();
        }
    }

    function buildSuggestions($component, suggestions) {
        var $list = $component.find('.search-field--results--suggestions .list');
        $list.empty();

        $.each(suggestions, function(index, suggestion) {
           $list.append($('<li>').text(suggestion));
        });

        show($component.find('.search-field--results--suggestions'), suggestions.length > 0);

        return suggestions.length;
    }


    function buildResults($component, results) {
        var $list = $component.find('.search-field--results--results .list');
        $list.empty();

        $.each(results, function(index, result) {
            var $a = $('<a>').attr('href', result.url).text(result.title);
            $list.append($('<li>').append($a));
        });

        show($component.find('.search-field--results--results'), results.length > 0);

        return results.length;
    }

    $('body').on('keyup', '.search-field--input', function() {
        var $component = $(this).closest('.component'),
            $form = $(this).closest('form'),
            url = $component.data('url');

        $.get(url, $form.serialize(), function(data) {
            var count,
                $list = $component.find('.search-field--results');

            count = buildSuggestions($list, data.suggestions);
            count += buildResults($list, data.results);

            show($list, count > 0);
        });
    });
});