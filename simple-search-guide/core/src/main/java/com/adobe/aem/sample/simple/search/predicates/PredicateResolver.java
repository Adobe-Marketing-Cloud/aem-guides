package com.adobe.aem.sample.simple.search.predicates;

import aQute.bnd.annotation.ProviderType;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;
import java.util.Map;

@ProviderType
public interface PredicateResolver {
    Map<String, String> getRequestPredicates(SlingHttpServletRequest request);

    List<PredicateGroup> getPredicateGroups(SlingHttpServletRequest request);
}
