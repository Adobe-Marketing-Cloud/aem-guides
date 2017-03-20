package com.adobe.aem.sample.simple.search.predicates;

import aQute.bnd.annotation.ConsumerType;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;
import java.util.Map;

@ConsumerType
public interface PredicateFactory {
    Map<String, String> getRequestPredicate(SlingHttpServletRequest request);

    List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request);

    String getTitle();

    String getName();
}
