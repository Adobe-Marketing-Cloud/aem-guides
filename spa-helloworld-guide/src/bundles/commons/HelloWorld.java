package com.adobe.cq.sample.spa.commons.impl.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = {ComponentExporter.class},
        resourceType = HelloWorld.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class HelloWorld implements ComponentExporter {

    static final String RESOURCE_TYPE = "we-retail-journal/global/components/helloworld";

    private static final String PREPEND_MSG = "Hello";

    @ValueMapValue
    private String message;

    public String getDisplayMessage() {
        if(message != null && message.length() > 0) {
            return PREPEND_MSG + " "  + message;
        }
        return null;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}
