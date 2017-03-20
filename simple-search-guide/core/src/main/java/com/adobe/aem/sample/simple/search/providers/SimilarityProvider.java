package com.adobe.aem.sample.simple.search.providers;

import aQute.bnd.annotation.ProviderType;
import com.adobe.aem.sample.simple.search.SearchResult;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import java.util.List;

@ProviderType
public interface SimilarityProvider {
    List<SearchResult> findSimilar(ResourceResolver resourceResolver, String similarToPath) throws RepositoryException;
}
