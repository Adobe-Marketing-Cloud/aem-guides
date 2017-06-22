package com.adobe.aem.guides.wknd.models.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import com.adobe.aem.guides.wknd.models.Footer;
import com.adobe.aem.guides.wknd.models.NavigationItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Footer.class, resourceType = "wknd/components/structure/footer")
@Exporter(name = "jackson", extensions = "json")
public class FooterImpl implements Footer {

	protected static final boolean PROP_SHOW_HIDDEN_DEFAULT = false;
	protected static final int PROP_START_LEVEL_DEFAULT = 2;

	@ScriptVariable
	private Style currentStyle;

	@ScriptVariable
	private Page currentPage;

	private int startLevel;
	private List<NavigationItem> items;

	@PostConstruct
	private void initModel() {
		startLevel = currentStyle.get(PN_START_LEVEL, PROP_START_LEVEL_DEFAULT);
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
		Page rootPage = currentPage.getAbsoluteParent(startLevel);
		String currentPagePath = currentPage.getPath();
		if (rootPage != null) {
			Iterator<Page> childPages = rootPage.listChildren();
			while (childPages.hasNext()) {
				Page navigationPage = childPages.next();

				// if navigation page is the current page
				boolean isActivePage = navigationPage.equals(currentPage);

				// if currentPage is a descendant of Navigation Page
				boolean isHierarchyActive = currentPagePath.startsWith(navigationPage.getPath());

				// don't include pages marked to be hidden in navigation
				if (!navigationPage.isHideInNav()) {
					//look for NavigationTitle if blank, fall back to jcr:title
					String text = StringUtils.isNotBlank(navigationPage.getNavigationTitle()) ? navigationPage.getNavigationTitle() : navigationPage.getTitle();
					NavigationItem navItem = new NavigationItemImpl(navigationPage, isActivePage, isHierarchyActive, null, navigationPage.getPath(), text);
					items.add(navItem);
				}
			} // end while
		} // end if
	}

}
