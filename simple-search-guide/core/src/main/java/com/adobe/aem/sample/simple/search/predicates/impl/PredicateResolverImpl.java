package com.adobe.aem.sample.simple.search.predicates.impl;

import com.adobe.aem.sample.simple.search.predicates.PredicateFactory;
import com.adobe.aem.sample.simple.search.predicates.PredicateGroup;
import com.adobe.aem.sample.simple.search.predicates.PredicateResolver;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@References({
        @Reference(
                referenceInterface = PredicateFactory.class,
                policy = ReferencePolicy.DYNAMIC,
                cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
})
@Service
public class PredicateResolverImpl implements PredicateResolver {
    final ConcurrentHashMap<String, PredicateFactory> factories = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(PredicateResolverImpl.class);

    public Map<String, String> getRequestPredicates(SlingHttpServletRequest request) {
        final Map<String, String> params = new HashMap<String, String>();

        for (final PredicateFactory factory : factories.values()) {
            params.putAll(factory.getRequestPredicate(request));
        }

        log.info(params.toString().replace("]","\n"));

        return params;
    }

    public List<PredicateGroup> getPredicateGroups(SlingHttpServletRequest request) {
        final List<PredicateGroup> predicateGroups = new ArrayList<PredicateGroup>();

        for (final PredicateFactory factory : factories.values()) {
            predicateGroups.add(new PredicateGroupImpl(factory.getTitle(), factory.getName(), factory.getPredicateOptions(request)));
        }

        return predicateGroups;
    }

    protected final void bindPredicateFactory(final PredicateFactory service, final Map<Object, Object> props) {
        final String type = PropertiesUtil.toString(props.get("service.pid"), null);
        if (type != null) {
            this.factories.put(type, service);
        }
    }

    protected final void unbindPredicateFactory(final PredicateFactory service, final Map<Object, Object> props) {
        final String type = PropertiesUtil.toString(props.get("service.pid"), null);
        if (type != null) {
            this.factories.remove(type);
        }
    }
}
