package com.adobe.aem.sample.simple.search.providers.impl;

import com.adobe.aem.sample.simple.search.SearchResult;
import com.adobe.aem.sample.simple.search.providers.SimilarityProvider;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.text.Text;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
public class SimilarityProviderImpl implements SimilarityProvider {
    private static final Logger log = LoggerFactory.getLogger(SimilarityProviderImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ModelFactory modelFactory;

    @Override
    public List<SearchResult> findSimilar(ResourceResolver resourceResolver, String similarToPath) throws RepositoryException {

        /**
         * Define the query
         **/
        final Map<String, String> params = new HashMap<String, String>();

        // Path level -> /content/docs/en/aem/6-3
        String searchPath = Text.getAbsoluteParent(similarToPath, 4);
        
        params.put("path", StringUtils.defaultIfEmpty(searchPath, "/content"));
        params.put("type", "cq:PageContent");

        params.put("similar", similarToPath + "/jcr:content");
        params.put("similar.local", ".");

        params.put("p.limit", "6");
        params.put("p.guessTotal", "true");

        /**
         * Execute the Query
         **/
        final Query query = queryBuilder.createQuery(PredicateGroup.create(params), resourceResolver.adaptTo(Session.class));

        /**
         * Convert the search-field-results into Search Result models
         **/
        final List<SearchResult> searchResults = new ArrayList<SearchResult>();

        for (Hit hit : query.getResult().getHits()) {
            try {
                if (!StringUtils.equals(similarToPath, hit.getResource().getParent().getPath())) {
                    // Don't include the similarToPath in the results
                    final SearchResult searchResult = modelFactory.createModel(hit.getResource().getParent(), SearchResult.class);
                    searchResults.add(searchResult);
                    log.debug("Added hit [ {} ] to search results", hit.getPath());
                }
            } catch (Exception e) {
                log.warn("Unable to adapt this hit's resource to a Search Result", e);
            }
        }

        return searchResults;
    }
}
