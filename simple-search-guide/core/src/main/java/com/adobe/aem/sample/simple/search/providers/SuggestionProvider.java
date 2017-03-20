package com.adobe.aem.sample.simple.search.providers;

import aQute.bnd.annotation.ProviderType;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import java.util.List;

@ProviderType
public interface SuggestionProvider {
    List<String> suggest(ResourceResolver resourceResolver, String path, String nodeType, String term, int limit) throws RepositoryException;
}
