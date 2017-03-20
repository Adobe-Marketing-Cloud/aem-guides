package com.adobe.aem.sample.simple.search;

import aQute.bnd.annotation.ProviderType;

import java.util.List;

@ProviderType
public interface SearchResults {
    List<SearchResult> getResults();

    List<SearchResultsPagination> getPagination();

    String getSearchTerm();

    long getTimeTaken();
    
    String getResultTotal();
}
