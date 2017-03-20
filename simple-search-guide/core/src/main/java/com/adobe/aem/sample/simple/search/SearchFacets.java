package com.adobe.aem.sample.simple.search;

import aQute.bnd.annotation.ProviderType;
import com.adobe.aem.sample.simple.search.predicates.PredicateGroup;

import java.util.List;

@ProviderType
public interface SearchFacets {
    List<PredicateGroup> getPredicateGroups();
}
