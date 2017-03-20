package com.adobe.aem.sample.simple.search.predicates.impl;

import com.adobe.aem.sample.simple.search.predicates.PredicateFactory;
import com.adobe.aem.sample.simple.search.predicates.PredicateOption;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class LimitPredicateFactoryImpl implements PredicateFactory {
    private static final Logger log = LoggerFactory.getLogger(LimitPredicateFactoryImpl.class);

    public static final String PN_LIMIT = "limit";
    public static final int DEFAULT_LIMIT = 10;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return PN_LIMIT;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        final int limit = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_LIMIT, DEFAULT_LIMIT);

        params.put("p.limit", String.valueOf(limit));

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
