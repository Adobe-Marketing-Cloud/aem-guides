package com.adobe.aem.sample.simple.search.impl;

import com.adobe.aem.sample.simple.search.SearchResultsPagination;

public class SearchResultsPaginationImpl implements SearchResultsPagination {
    private String label;
    private long offset;
    private boolean active;
    private boolean disabled;

    public SearchResultsPaginationImpl(long offset, String label, boolean active, boolean disabled) {
        this.label = label;
        this.offset = offset;
        this.active = active;
        this.disabled = disabled;
    }

    public long getOffset() {
        return offset;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isDisabled() {
        return disabled;
    }
}