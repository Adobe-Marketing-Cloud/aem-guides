package com.adobe.aem.guides.assetinsights.models.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.SightlyWCMMode;
import com.adobe.cq.wcm.core.components.internal.Constants;
import com.adobe.cq.wcm.core.components.internal.servlets.AdaptiveImageServlet;
import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.commons.DownloadResource;
import com.day.cq.commons.ImageResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;

/***
 * Implementation of Core Component Image class
 * adds additional method getAssetId() to support Asset Insight feature
 *
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = Image.class, resourceType = ImageImpl.RESOURCE_TYPE)
@Exporter(name = Constants.EXPORTER_NAME, extensions = Constants.EXPORTER_EXTENSION)
public class ImageImpl implements Image {

    public static final String RESOURCE_TYPE = "aem-guides/asset-insights/components/content/image";
    public static final String DEFAULT_EXTENSION = "jpeg";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageImpl.class);
    private static final String DOT = ".";
    private static final String MIME_TYPE_IMAGE_JPEG = "image/jpeg";
    private static final String PN_DESIGN_ASSET_INSIGHTS_ENABLED = "enableAssetInsights";
    
    @Self
    private SlingHttpServletRequest request;

    @Inject
    private Resource resource;
    
    @Inject
    private ResourceResolver resourceResolver;

    @ScriptVariable
    private PageManager pageManager;

    @ScriptVariable
    private SightlyWCMMode wcmmode;

    @Inject
    @Source("osgi-services")
    private MimeTypeService mimeTypeService;

    @ValueMapValue(name = DownloadResource.PN_REFERENCE, injectionStrategy = InjectionStrategy.OPTIONAL)
    private String fileReference;

    @ValueMapValue(name = PN_IS_DECORATIVE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = false)
    private boolean isDecorative;

    @ValueMapValue(name = PN_DISPLAY_POPUP_TITLE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(booleanValues = false)
    private boolean displayPopupTitle;

    @ValueMapValue(name = ImageResource.PN_ALT, injectionStrategy = InjectionStrategy.OPTIONAL)
    private String alt;

    @ValueMapValue(name = JcrConstants.JCR_TITLE, injectionStrategy = InjectionStrategy.OPTIONAL)
    private String title;

    @ValueMapValue(name = ImageResource.PN_LINK_URL, injectionStrategy = InjectionStrategy.OPTIONAL)
    private String linkURL;

    private String extension;
    private String src;
    private String[] smartImages = new String[]{};
    private int[] smartSizes = new int[0];
    private String json;
    private String assetId;

    private boolean disableLazyLoading;

    @PostConstruct
    private void initModel() {
        boolean hasContent = false;
        if (StringUtils.isNotEmpty(fileReference)) {
            int dotIndex;
            if ((dotIndex = fileReference.lastIndexOf(DOT)) != -1) {
                extension = fileReference.substring(dotIndex + 1);
            }
            hasContent = true;
        } else {
            Resource file = resource.getChild(DownloadResource.NN_FILE);
            if (file != null) {
                extension = mimeTypeService.getExtension(
                        PropertiesUtil.toString(file.getResourceMetadata().get(ResourceMetadata.CONTENT_TYPE), MIME_TYPE_IMAGE_JPEG)
                );
                hasContent = true;
            }
        }
        if (hasContent) {
            long lastModifiedDate = 0;
            if (!wcmmode.isDisabled()) {
                ValueMap properties = resource.getValueMap();
                Calendar lastModified = properties.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
                if (lastModified == null) {
                    lastModified = properties.get(NameConstants.PN_PAGE_LAST_MOD, Calendar.class);
                }
                if (lastModified != null) {
                    lastModifiedDate = lastModified.getTimeInMillis();
                }
            }
            if (extension.equalsIgnoreCase("tif") || extension.equalsIgnoreCase("tiff")) {
                extension = DEFAULT_EXTENSION;
            }
            ContentPolicyManager policyManager = resourceResolver.adaptTo(ContentPolicyManager.class);
            ContentPolicy contentPolicy = policyManager.getPolicy(resource);
            if (contentPolicy != null) {
                disableLazyLoading = contentPolicy.getProperties().get(PN_DESIGN_LAZY_LOADING_ENABLED, false);
            }
            Set<Integer> supportedRenditionWidths = getSupportedRenditionWidths(contentPolicy);
            smartImages = new String[supportedRenditionWidths.size()];
            smartSizes = new int[supportedRenditionWidths.size()];
            int index = 0;
            String escapedResourcePath = Text.escapePath(resource.getPath());
            for (Integer width : supportedRenditionWidths) {
                smartImages[index] = request.getContextPath() + escapedResourcePath + DOT + AdaptiveImageServlet.DEFAULT_SELECTOR + DOT +
                        width + DOT + extension + (!wcmmode.isDisabled() && lastModifiedDate > 0 ? "/" + lastModifiedDate + DOT + extension
                        : "");
                smartSizes[index] = width;
                index++;
            }
            if (smartSizes.length == 0 || smartSizes.length >= 2) {
                src = request.getContextPath() + escapedResourcePath + DOT + AdaptiveImageServlet.DEFAULT_SELECTOR + DOT + extension +
                        (!wcmmode.isDisabled() && lastModifiedDate > 0 ? "/" + lastModifiedDate + DOT + extension : "");
            } else if (smartSizes.length == 1) {
                src = request.getContextPath() + escapedResourcePath + DOT + AdaptiveImageServlet.DEFAULT_SELECTOR + DOT + smartSizes[0] +
                        DOT + extension + (!wcmmode.isDisabled() && lastModifiedDate > 0 ? "/" + lastModifiedDate + DOT + extension : "");
            }
            if (!isDecorative) {
                Page page = pageManager.getPage(linkURL);
                if (page != null) {
                    String vanityURL = page.getVanityUrl();
                    linkURL = (vanityURL == null ? linkURL + ".html" : vanityURL);
                }
            } else {
                linkURL = null;
            }
            
            populateAssetId(contentPolicy);
            buildJson();
        }
    }

    @Override
    public String getSrc() {
        return src;
    }

    @Override
    public boolean displayPopupTitle() {
        return displayPopupTitle;
    }

    @Override
    public String getAlt() {
        return alt;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getLink() {
        return linkURL;
    }

    @Override
    public String getFileReference() {
        return fileReference;
    }

    @Override
    public String getJson() {
        return json;
    }
    
    public String getAssetId() {
    	return assetId;
    }

    private void buildJson() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(Image.JSON_SMART_SIZES, new JSONArray(Arrays.asList(ArrayUtils.toObject(smartSizes))));
        objectMap.put(Image.JSON_SMART_IMAGES, new JSONArray(Arrays.asList(smartImages)));
        objectMap.put(Image.JSON_LAZY_ENABLED, !disableLazyLoading);
        json = new JSONObject(objectMap).toString();
    }

    private Set<Integer> getSupportedRenditionWidths(ContentPolicy contentPolicy) {
        Set<Integer> allowedRenditionWidths = new TreeSet<>();
        if (contentPolicy != null) {
            String[] supportedWidthsConfig = contentPolicy.getProperties().get(PN_DESIGN_ALLOWED_RENDITION_WIDTHS, new String[0]);
            for (String width : supportedWidthsConfig) {
                try {
                    allowedRenditionWidths.add(Integer.parseInt(width));
                } catch (NumberFormatException e) {
                    LOGGER.error(String.format("Invalid width detected (%s) for content policy configuration.", width), e);
                }
            }
        }
        return allowedRenditionWidths;
    }
    
    private void populateAssetId(ContentPolicy contentPolicy) {
    	
    	if(StringUtils.isNotEmpty(fileReference) && contentPolicy != null) {
    		boolean enableAssetInsights = contentPolicy.getProperties().get(PN_DESIGN_ASSET_INSIGHTS_ENABLED, false);
    		if(enableAssetInsights) {
    			Resource assetResource = resourceResolver.getResource(fileReference);
    			if(assetResource != null) {
    				Asset asset = DamUtil.resolveToAsset(assetResource);
    				if(asset != null) {
    					assetId = asset.getID();
    				}
    			}
    		}
    	}	
    }
}