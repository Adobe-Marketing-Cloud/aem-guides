package com.adobe.aem.sample.simple.search.providers;

import aQute.bnd.annotation.ProviderType;
import com.adobe.aem.sample.simple.search.SearchResultsPagination;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.search.result.SearchResult;

import java.util.List;
import java.util.Map;

@ProviderType
public interface SearchProvider {
	SearchResult search(ResourceResolver resourceResolver, Map<String, String> predicates);

	List<SearchResultsPagination> buildPagination(SearchResult result, String previousLabel, String nextLabel);

    List<com.adobe.aem.sample.simple.search.SearchResult> buildSearchResults(SearchResult result);
}
