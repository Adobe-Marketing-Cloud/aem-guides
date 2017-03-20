package com.adobe.aem.sample.simple.search.impl;

import com.adobe.aem.sample.simple.search.SearchResult;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
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
        resourceType = "dam:Asset"
)
public class AssetSearchResultImpl implements SearchResult {

    @Self
    private Resource resource;

    @Inject
    private ResourceResolver resourceResolver;

    @PostConstruct
    protected void initModel() {
        this.asset = DamUtil.resolveToAsset(resource);
    }

    private Asset asset;

    private List<String> excerpts = new ArrayList<String>();

    public ContentType getContentType() {
        return ContentType.ASSET;
    }

    public String getPath() {
        return asset.getPath();
    }

    @Override
    public String getURL() {
        return getPath();
    }

    public String getTitle() {
        return StringUtils.defaultIfBlank(StringUtils.defaultIfBlank(asset.getMetadataValue("dc:title"), asset.getName()), asset.getName());
    }

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String getDescription() {
        if (this.excerpts.size() > 2) {
            return StringUtils.trim(DESCRIPTION_ELLIPSIS + StringUtils.join(this.excerpts, DESCRIPTION_ELLIPSIS) + DESCRIPTION_ELLIPSIS);
        } else {
            return StringUtils.left(asset.getMetadataValue("dc:description"), DESCRIPTION_MAX_LENGTH);
        }
    }

    @Override
    public List<String> getTagIds() {
        final TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        final List<String> tagIds = new ArrayList<String>();

        for (Tag tag : tagManager.getTags(resource)) {
            tagIds.add(tag.getTagID());
        }

        return tagIds;
    }

    @Override
    public void setExcerpts(Collection<String> excerpts) {
        this.excerpts = new ArrayList<String>(excerpts);
    }
}
