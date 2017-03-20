package com.adobe.aem.sample.simple.search.providers.impl;


import com.adobe.aem.sample.simple.search.SearchResultsPagination;
import com.adobe.aem.sample.simple.search.impl.SearchResultsPaginationImpl;
import com.adobe.aem.sample.simple.search.providers.SearchProvider;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.ResultPage;
import com.day.cq.search.result.SearchResult;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service
public class SearchProviderImpl implements SearchProvider {
    private static final Logger log = LoggerFactory.getLogger(SearchProviderImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ModelFactory modelFactory;

    public SearchResult search(ResourceResolver resourceResolver, Map<String, String> predicates) {
        final Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), resourceResolver.adaptTo(Session.class));

        return query.getResult();
    }

    @Override
    public final List<com.adobe.aem.sample.simple.search.SearchResult> buildSearchResults(com.day.cq.search.result.SearchResult result) {
        final List<com.adobe.aem.sample.simple.search.SearchResult> searchResults = new ArrayList<com.adobe.aem.sample.simple.search.SearchResult>();

        for (Hit hit : result.getHits()) {
            try {
                final com.adobe.aem.sample.simple.search.SearchResult searchResult = modelFactory.createModel(hit.getResource(), com.adobe.aem.sample.simple.search.SearchResult.class);
                // Augment the Search Result model with attributes that cannot be obtained directly from the resource
                List<String> excerpts = new ArrayList<>();
                excerpts.add(hit.getExcerpt());
                searchResult.setExcerpts(excerpts);

                searchResults.add(searchResult);
                log.debug("Added hit [ {} ] to search results", hit.getPath());
            } catch (Exception e) {
                log.warn("Unable to adapt this hit's resource to a Search Result", e);
            }
        }
        return searchResults;
    }

    @Override
    public final List<SearchResultsPagination> buildPagination(com.day.cq.search.result.SearchResult result, String previousLabel, String nextLabel) {
        final List<SearchResultsPagination> pagination = new ArrayList<SearchResultsPagination>();
        long hitNum = result.getHitsPerPage();

        // Populate the Previous page
        ResultPage previousPageResult = result.getPreviousPage();
        if (previousPageResult != null) {
            SearchResultsPaginationImpl previousPage = new SearchResultsPaginationImpl(previousPageResult.getStart(), previousLabel, previousPageResult.isCurrentPage(), previousPageResult.isCurrentPage());
            pagination.add(previousPage);
        }

        // Populate the numbered pages
        for (ResultPage page : result.getResultPages()) {
            SearchResultsPaginationImpl currOption = new SearchResultsPaginationImpl(page.getStart(),
                    String.valueOf((page.getStart() / hitNum) + 1), page.isCurrentPage(), page.isCurrentPage());
            pagination.add(currOption);
        }

        // Populate the Next page
        ResultPage nextPageResult = result.getNextPage();
        if (nextPageResult != null) {
            SearchResultsPaginationImpl nextPage = new SearchResultsPaginationImpl(nextPageResult.getStart(), nextLabel, nextPageResult.isCurrentPage(), nextPageResult.isCurrentPage());
            pagination.add(nextPage);
        }

        return pagination;
    }
}
