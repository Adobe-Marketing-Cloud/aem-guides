package com.adobe.aem.guides.wknd.models.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.aem.guides.wknd.models.Header;
import com.adobe.aem.guides.wknd.models.NavigationItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Header.class, resourceType = HeaderImpl.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class HeaderImpl implements Header {

	protected static final String RESOURCE_TYPE = "wknd/components/structure/header";
	private static final Logger log = LoggerFactory.getLogger(HeaderImpl.class);
	private static final String DEFAULT_REL_PATH = "root/header";
	
	@ScriptVariable
	private Style currentStyle;
	
	@ScriptVariable
	private Page currentPage;
	
	@ScriptVariable
	private PageManager pageManager;

	private String relativeHeaderPath;
	private Resource headerResource;
	private List<NavigationItem> items;
	private String rootPath;

	@PostConstruct
	private void initModel() {
	    relativeHeaderPath = currentStyle.get(PN_POLICY_REL_PATH, DEFAULT_REL_PATH);
		setHeaderResource();
	}

	/**
	 * Look beneath current resource for Header resource
	 * if not defined, iterate up content tree to find header resource
	 */
	private void setHeaderResource() {
		//target page where navigation is defined
		Page targetPage = currentPage;
		headerResource = targetPage.getContentResource(relativeHeaderPath);
		
		//iterate until we find a header resource that is not null or finish traversing the hierarchy
		while(isEmptyHeader(headerResource) && targetPage != null) {
			targetPage = targetPage.getParent();
			if(targetPage != null) {
				headerResource = targetPage.getContentResource(relativeHeaderPath);
			}
		}
	}
	
	/**
	 * Return true if the header resource is null or not populated
	 * @param headerResource
	 * @return
	 */
	private boolean isEmptyHeader(Resource headerResource) {
		if(headerResource != null) {
			String rootPath = headerResource.getValueMap().get(PN_ROOT_PATH, String.class);
			if(headerResource.hasChildren() || StringUtils.isNotBlank(rootPath)) {
				return false;
			}
		}	
		return true;
	}
	
	@Override
	public Collection<NavigationItem> getItems() {
		if (items == null) {
			items = new ArrayList<>();
			createNavigation();
		}
		return items;
	}

	private void createNavigation() {
		if(headerResource != null) {
			//get multi-value properties beneath header resource
			Resource pagesRes = headerResource.getChild(PAGES_NODE);
			if(pagesRes != null) {
				Iterator<Resource> childResources = pagesRes.listChildren();
				while(childResources.hasNext()) {
					NavigationItem navItem = createNavItem(childResources.next());
					if(navItem != null) {
						items.add(navItem);
					}
				}
			}
		}
		
	}


	/***
	 * evaluates resource to determine if the link is an external url or a relative page
	 * @param resource
	 * @return NavigationItem
	 */
	private NavigationItem createNavItem(Resource resource) {
	
		ValueMap linkProperties = resource.getValueMap();
		String url = linkProperties.get(PN_PATH, String.class);
		String icon = linkProperties.get(PN_ICON, String.class);
		String text = linkProperties.get(PN_TEXT, String.class);
		boolean isActive = false;
		boolean isHierarchyActive = false;
		Page navPage = null;
		
		if(StringUtils.isNotBlank(url)) {
			
			//indicates that the path is a relative path
			if(url.startsWith("/content")) {
				 navPage = pageManager.getPage(url);
				if(StringUtils.isBlank(text)) {
					//use nav title or fall back to title
					text = StringUtils.isNotBlank(navPage.getNavigationTitle()) ? navPage.getNavigationTitle() : navPage.getTitle();
				}
				isActive = currentPage.getPath().equals(url) ? true: false;
				isHierarchyActive = currentPage.getPath().startsWith(url) ? true : false;
			}
			NavigationItem navItem = new NavigationItemImpl(navPage, isActive, isHierarchyActive, icon, url, text);
			return navItem;
		}
		
		return null;
	}

	@Override
	public String getNavigationRoot() {
		if(rootPath == null) {
			if(headerResource != null) {
				rootPath = headerResource.getValueMap().get(PN_ROOT_PATH, String.class);
			}
		}
		return rootPath;
	}

}
