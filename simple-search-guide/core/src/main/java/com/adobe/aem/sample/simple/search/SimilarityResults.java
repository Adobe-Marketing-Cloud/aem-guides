package com.adobe.aem.sample.simple.search;

import aQute.bnd.annotation.ProviderType;

import javax.jcr.RepositoryException;
import java.util.List;

@ProviderType
public interface SimilarityResults {
    List<SearchResult> getResults() throws RepositoryException;

    long getTimeTaken();
}
