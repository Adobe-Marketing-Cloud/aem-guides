package com.adobe.aem.sample.simple.search.predicates.impl;

import com.adobe.aem.sample.simple.search.predicates.PredicateFactory;
import com.adobe.aem.sample.simple.search.predicates.PredicateOption;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import org.apache.commons.lang.StringUtils;
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
public class PathsPredicateFactoryImpl implements PredicateFactory {
    public static final String PN_SEARCH_PATHS = "searchPaths";
    public static final String DEFAULT_SEARCH_PATH = "/content";
    public static final String SAFEGUARD_SEARCH_PATH = "/content";
    private static final Logger log = LoggerFactory.getLogger(PathsPredicateFactoryImpl.class);

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return PN_SEARCH_PATHS;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        final String[] paths = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_SEARCH_PATHS, new String[]{DEFAULT_SEARCH_PATH});

        for (String path : paths) {
            if (StringUtils.startsWith(path, SAFEGUARD_SEARCH_PATH)) {
                params.put("path", path);
            }
        }

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
