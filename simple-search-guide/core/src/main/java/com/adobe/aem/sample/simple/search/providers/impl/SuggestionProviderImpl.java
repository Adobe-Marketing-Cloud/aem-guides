package com.adobe.aem.sample.simple.search.providers.impl;

import com.adobe.aem.sample.simple.search.providers.SuggestionProvider;
import com.day.cq.search.QueryBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Service
public class SuggestionProviderImpl implements SuggestionProvider {
    private static final Logger log = LoggerFactory.getLogger(SuggestionProviderImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ModelFactory modelFactory;

    @Override
    public List<String> suggest(ResourceResolver resourceResolver, String path, String nodeType, String term, int limit) throws RepositoryException {
        final Set<String> suggestionsKeys = new HashSet<String>();
        final List<String> suggestions = new ArrayList<>();

        if (StringUtils.isBlank(term)) {
            return suggestions;
        }

        nodeType = StringUtils.defaultIfEmpty(nodeType, JcrConstants.NT_HIERARCHYNODE);
        path = StringUtils.defaultIfEmpty(path, "/");

        final String statement = String.format("SELECT [rep:suggest()] FROM [%s] WHERE ISDESCENDANTNODE('%s') AND SUGGEST('%s')", escape(nodeType), escape(path), escape(term));

        final QueryManager queryManager = resourceResolver.adaptTo(Session.class).getWorkspace().getQueryManager();
        final QueryResult result = queryManager.createQuery(statement, javax.jcr.query.Query.JCR_SQL2).execute();
        final RowIterator rows = result.getRows();

        int count = 0;
        while (rows.hasNext()) {
            final Row row = rows.nextRow();

            String suggestion = row.getValue("rep:suggest()").getString();
            String key = getUniqueSuggestionKey(suggestion);

            if (suggestionsKeys.contains(key)) {
                continue;
            }

            suggestionsKeys.add(key);
            suggestions.add(StringUtils.lowerCase(suggestion));
            if (limit > 0 && ++count >= limit) {
                break;
            }
        }

        return suggestions;
    }

    private String escape(String str) {
        return StringUtils.stripToEmpty(str).replaceAll("'", "''");
    }


    /**
     * This create a key to help normalize and de-dupe results.
     * - turns to lowercase
     * - turns all ' ' to -
     * - reduces all multiple - segments into a single - (ex. --- becomes -)
     * *
     *
     * @param suggestion the suggestion to create a unique key for.
     * @return the suggestions key.
     */
    private String getUniqueSuggestionKey(String suggestion) {
        suggestion = StringUtils.lowerCase(suggestion);
        suggestion = StringUtils.trim(suggestion);
        suggestion = suggestion.replaceAll("/[^A-Za-z0-9 ]/", "-");
        suggestion = suggestion.replaceAll("-+", "-");
        return suggestion;
    }
}
