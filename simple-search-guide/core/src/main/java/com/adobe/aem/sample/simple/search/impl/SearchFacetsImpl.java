package com.adobe.aem.sample.simple.search.impl;

import com.adobe.aem.sample.simple.search.SearchFacets;
import com.adobe.aem.sample.simple.search.predicates.PredicateGroup;
import com.adobe.aem.sample.simple.search.predicates.PredicateResolver;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchFacets.class
)
public class SearchFacetsImpl implements SearchFacets {
    private static final Logger log = LoggerFactory.getLogger(SearchFacetsImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private PredicateResolver predicateResolver;

    public List<PredicateGroup> getPredicateGroups() {
        return predicateResolver.getPredicateGroups(request);
    }
}
