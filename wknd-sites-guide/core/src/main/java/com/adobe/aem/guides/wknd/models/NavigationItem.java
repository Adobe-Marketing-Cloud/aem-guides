package com.adobe.aem.guides.wknd.models;

import com.day.cq.wcm.api.Page;

public interface NavigationItem {
	
	 /**
     * @return The {@link Page} contained in this navigation item.
     */
    Page getPage();

    /**
     * Gets the active information of the current page
     *
     * @return true if it is the current page
     */
    boolean isActive();
    
    /**
     * Gets the hierarchy information  of the current page
     * 
     * @return true if the current page is a descendant of the navigation page
     */
    boolean isHierarchyActive();
    
    /**
     * Gets the icon associated with the navigation page
     * 
     * @return the icon associated with the navigation page
     */
    String getIcon();
    
    /**
     * Gets the URL associated with the navigation. Could be a relative path or an absolute URL.
     * @return
     */
    String getUrl();
    
    /**
     * The String value to populate the navigation link text
     * @return
     */
    String getText();

}
