package com.adobe.aem.sample.simple.search.predicates.impl;

import com.adobe.aem.sample.simple.search.predicates.PredicateFactory;
import com.adobe.aem.sample.simple.search.predicates.PredicateOption;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class FullltextPredicateFactoryImpl implements PredicateFactory {
    public static final String REQUEST_PARAM = "q";

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return REQUEST_PARAM;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        String term = request.getParameter(REQUEST_PARAM);
        if (StringUtils.isNotBlank(term)) {
            params.put("fulltext", term);
        }

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
