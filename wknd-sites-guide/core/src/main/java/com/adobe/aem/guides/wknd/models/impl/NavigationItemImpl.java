package com.adobe.aem.guides.wknd.models.impl;

import com.adobe.aem.guides.wknd.models.NavigationItem;
import com.day.cq.wcm.api.Page;

public class NavigationItemImpl implements NavigationItem {
	
	private Page page;
    private boolean active;
    private boolean hierarchyActive;
    private String icon;
    private String url;
    private String text;
    
    public NavigationItemImpl(Page page, boolean active, boolean hierarchyActive, String icon, String url, String text) {
        this.page = page;
        this.active = active;
        this.hierarchyActive = hierarchyActive;
        this.icon = icon;
        this.url = url;
        this.text = text;
    }

	@Override
	public Page getPage() {
		return page;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isHierarchyActive() {
		return hierarchyActive;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getText() {
		return text;
	}

}
