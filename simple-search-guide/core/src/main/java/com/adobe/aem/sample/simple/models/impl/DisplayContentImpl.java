package com.adobe.aem.sample.simple.models.impl;

import com.adobe.aem.sample.simple.models.DisplayContent;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = DisplayContent.class
)
public class DisplayContentImpl implements DisplayContent {
    private static final Logger log = LoggerFactory.getLogger(DisplayContentImpl.class);

    private static final String CONTENT_BODY_RESOURCE = "contentbody";
    private static final String TITLE_RESOURCE = "docs/components/title";
    private static final String REMARK_RESOURCE = "docs/components/remark";

    private List<String> contentList;

    @ScriptVariable
    private Page currentPage;

    @Override
    public List<String> getDocsContent() {
        Resource contentBodyRes = currentPage.getContentResource(CONTENT_BODY_RESOURCE);
        contentList = new ArrayList<String>();
        buildList(contentBodyRes);
        return contentList;
    }

    private void buildList(Resource docsResource) {
        if (docsResource == null) {
            log.warn("Cannot document page content from null resource");
            return;
        }

        Iterator<Resource> childRes = docsResource.listChildren();

        while (childRes.hasNext()) {
            Resource currentRes = childRes.next();

            ValueMap properties = currentRes.adaptTo(ValueMap.class);
            String htmlContent = properties.get("text", String.class);
            String textIsRich = properties.get("textIsRich", String.class);

            // Special handling Title resource
            if (TITLE_RESOURCE.equals(currentRes.getResourceType())) {
                String title = properties.get("jcr:title", String.class);
                String size = properties.get("type", "small");

                if (StringUtils.isNotBlank(title) && size.equals("medium")) {
                    contentList.add("<h3>" + title + "</h3>");
                } else if (StringUtils.isNotBlank(title)) {
                    contentList.add("<h4>" + title + "</h4>");
                }
            }

            if (StringUtils.isNotBlank(htmlContent)
                    && "true".equals(textIsRich)
                    && !REMARK_RESOURCE.equals(currentRes.getResourceType())) {
                contentList.add(htmlContent);
            }

            //traverse children
            if (currentRes.hasChildren()) {
                buildList(currentRes);
            }
        }
    }
}
