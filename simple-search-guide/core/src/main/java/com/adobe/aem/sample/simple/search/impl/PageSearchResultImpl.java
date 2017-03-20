package com.adobe.aem.sample.simple.search.impl;

import com.adobe.aem.sample.simple.search.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Model(
        adaptables = Resource.class,
        adapters = SearchResult.class,
        resourceType = "cq:Page"
)
public class PageSearchResultImpl implements SearchResult {
    private static final String PAGE_EXTENSION = ".html";
    private static final String NBSP_HTML = "&nbsp;";

    @Self
    private Resource resource;

    @Inject
    private ResourceResolver resourceResolver;


    @PostConstruct
    protected void intialize() {
        this.page = resourceResolver.adaptTo(PageManager.class).getContainingPage(resource);
    }

    private Page page;

    private List<String> excerpts = new ArrayList<String>();

    public ContentType getContentType() {
        return ContentType.PAGE;
    }

    public String getPath() {
        return this.page.getPath();
    }

    public String getURL() {
        return getPath() + PAGE_EXTENSION;
    }

    public String getTitle() {
        return StringUtils.defaultIfBlank(StringUtils.defaultIfBlank(page.getPageTitle(), page.getTitle()), page.getName());
    }

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String getDescription() {
        String description = "";
        if (this.excerpts.size() > 0) {
            description = StringUtils.join(this.excerpts, DESCRIPTION_ELLIPSIS);
            if (StringUtils.isNotBlank(description)) {
                description = StringUtils.trim(description);
            }
        }

        if (StringUtils.isBlank(description)) {
            description = StringUtils.substringBeforeLast(StringUtils.left(page.getDescription(), DESCRIPTION_MAX_LENGTH), " ");
            if (StringUtils.isNotBlank(description)) {
                description += DESCRIPTION_ELLIPSIS;
            }
        }

        return description;
    }

    @Override
    public List<String> getTagIds() {
        final List<String> tagIds = new ArrayList<String>();

        for (Tag tag : page.getTags()) {
            tagIds.add(tag.getTagID());
        }

        return tagIds;
    }

    @Override
    public void setExcerpts(Collection<String> excerpts) {
        for (String excerpt : excerpts) {
            // Funny clean-up require as getExcerpt() can sometimes inject &nbsp; into the excerpt text..
            excerpt = StringUtils.replace(excerpt, NBSP_HTML, "");
            if (StringUtils.isNotBlank(excerpt)) {
                this.excerpts.add(StringUtils.trim(excerpt));
            }
        }
    }
}
