package com.adobe.aem.guides.wknd.models;

import java.util.Collection;

public interface Footer {
	
   /**
     * Name of the resource property that will indicate from which level starting from the current page the items from the collection
     * returned by {@link #getItems()} will be accumulated.
     */
    String PN_START_LEVEL = "startLevel";

    /**
     * Creates collection of pages(from site hierarchy of current page) for Global Navigation
     * Navigation Pages will be made up of direct children of navigation root specified by startLevel property
     * @return {@link Collection} of navigation items
     */
    Collection<NavigationItem> getItems();

}
