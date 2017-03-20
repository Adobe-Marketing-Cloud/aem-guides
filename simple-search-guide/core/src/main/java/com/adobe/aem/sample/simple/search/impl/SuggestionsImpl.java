package com.adobe.aem.sample.simple.search.impl;

import com.adobe.aem.sample.simple.search.Suggestions;
import com.adobe.aem.sample.simple.search.predicates.impl.PathsPredicateFactoryImpl;
import com.adobe.aem.sample.simple.search.providers.SuggestionProvider;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.day.cq.wcm.api.NameConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = Suggestions.class,
        resourceType = "simple/components/content/search/suggestions"
)
@Exporter(
        name = "jackson",
        selector = "suggestions",
        extensions = "json"
)
public class SuggestionsImpl implements Suggestions {
    private static final Logger log = LoggerFactory.getLogger(SuggestionsImpl.class);

    private static final String PN_SUGGESTIONS_LIMIT = "suggestionsLimit";
    private static final int DEFAULT_SUGGESTIONS_LIMIT = 5;

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private ResourceResolver resourceResolver;

    @Inject
    private SuggestionProvider suggestionProvider;

    @Inject
    private Resource resource;

    List<String> suggestions = Collections.emptyList();

    private long timeTaken = -1;

    @PostConstruct
    private void initModel() {
        final long start = System.currentTimeMillis();

        try {
            // This is required due to a bug in Sling Model Exporter; where 2 Sing Model Exporters cannot bind to the same resourceType
            final String searchPath = new ComponentInheritanceValueMap(resource).getInherited(PathsPredicateFactoryImpl.PN_SEARCH_PATHS, PathsPredicateFactoryImpl.DEFAULT_SEARCH_PATH);
            suggestions = suggestionProvider.suggest(resourceResolver, searchPath,
                                                NameConstants.NT_PAGE, getSearchTerm(),
                                                resource.getValueMap().get(PN_SUGGESTIONS_LIMIT, DEFAULT_SUGGESTIONS_LIMIT));
        } catch (RepositoryException e) {
            log.error("Could not collect suggestions for search term [ {} ]", getSearchTerm());
        }

        timeTaken = System.currentTimeMillis() - start;
    }

    public List<String> getSuggestions() {
       return suggestions;
    }

    @Override
    public String getSearchTerm() {
        return request.getParameter("q");
    }

    @Override
    public long getTimeTaken() {
        return timeTaken;
    }
}
