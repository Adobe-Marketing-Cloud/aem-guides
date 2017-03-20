package com.adobe.aem.sample.simple.search.predicates.impl;

import com.adobe.aem.sample.simple.search.predicates.PredicateFactory;
import com.adobe.aem.sample.simple.search.predicates.PredicateOption;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.NameConstants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class NodeTypesPredicateFactoryImpl implements PredicateFactory {
    public static final String PN_SEARCH_NODE_TYPES = "searchNodeTypes";

    public static final String[] ALLOWS_NODE_TYPES = new String[]{ NameConstants.NT_PAGE, DamConstants.NT_DAM_ASSET};

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return PN_SEARCH_NODE_TYPES;
    }

    @Override
    public Map<String, String> getRequestPredicate(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        final String[] nodeTypes = new ComponentInheritanceValueMap(request.getResource()).getInherited(PN_SEARCH_NODE_TYPES, new String[]{NameConstants.NT_PAGE});

        int count = 0;
        for (String nodeType : nodeTypes) {
            if (ArrayUtils.contains(ALLOWS_NODE_TYPES, nodeType)) {
                params.put("type", nodeType);
            }
        }

        return params;
    }

    @Override
    public List<PredicateOption> getPredicateOptions(SlingHttpServletRequest request) {
        return Collections.emptyList();
    }
}
