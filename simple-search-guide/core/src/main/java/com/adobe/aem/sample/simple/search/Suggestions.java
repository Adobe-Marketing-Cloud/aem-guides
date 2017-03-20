package com.adobe.aem.sample.simple.search;

import aQute.bnd.annotation.ProviderType;

import java.util.List;

@ProviderType
public interface Suggestions {
    List<String> getSuggestions();

    String getSearchTerm();

    long getTimeTaken();
}
