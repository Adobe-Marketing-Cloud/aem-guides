# Simple Search Project

[https://helpx.adobe.com/experience-manager/kt/sites/using/search-tutorial-develop.html](https://helpx.adobe.com/experience-manager/kt/sites/using/search-tutorial-develop.html)

## Search application overview

![Simple Search Application](https://helpx.adobe.com/experience-manager/kt/sites/using/search-tutorial-develop/_jcr_content/main-pars/image_2094777936.img.png/simple-search-application.png)

1. **HTL** defines the **HTML** presentation layer, and uses **Sling Models** to obtain the the results to display.
2. The **Sling Models** are Java data models that represent results.
3. The **Predicate Factory OSGi services** collect and sanitize query parameters from the HTTP Request or the configured resources.
4. Each **Provider OSGi service** is engages with AEM (QueryBuilder or Oak) to obtain results.
5. **QueryBuilder** converts the provided query parameters to a Xpath query and executes the query.
6. **Oak query engine** selects the best index for the query and collects results
7. Custom **JavaScript** makes AJAX calls to collect JSON results
    * Steps 2-6 in the above flow is invoked
8. The **Sling Model Exporter** framework serializes the Sling Model results into JSON, for DOM injection by the JavaScript
   * Powers Suggestions and Quick Links


## Search application code index

1. /apps/simple/components/content/search
	* com.adobe.aem.sample.simple.search
2. https://sling.apache.org/documentation/bundles/models.html
3. com.adobe.aem.sample.simple.search.predicates
4. com.adobe.aem.sample.simple.search.providers
   * http://docs.adobe.com/docs/en/aem/6-2/develop/search/querybuilder-api.html
5. http://jackrabbit.apache.org/oak/docs/query/query-engine.html
6. /apps/simple/components/content/search/clientlibs/js
7. com.adobe.aem.sample.simple 
   * https://sling.apache.org/documentation/bundles/models.html#exporter-framework-since-130


-----


## Building

This project uses Maven for building. Common commands:

From the root directory, run ``mvn -PautoInstallPackage clean install`` to build the bundle and content package and install to a CQ instance.

From the bundle directory, run ``mvn -PautoInstallBundle clean install`` to build *just* the bundle and install to a CQ instance.

## Using with AEM Developer Tools for Eclipse

To use this project with the AEM Developer Tools for Eclipse, import the generated Maven projects via the Import:Maven:Existing Maven Projects wizard. Then enable the Content Package facet on the _content_ project by right-clicking on the project, then select Configure, then Convert to Content Package... In the resulting dialog, select _src/main/content_ as the Content Sync Root.

## Using with VLT

To use vlt with this project, first build and install the package to your local CQ instance as described above. Then cd to `content/src/main/content/jcr_root` and run

    vlt --credentials admin:admin checkout -f ../META-INF/vault/filter.xml --force http://localhost:4502/crx

Once the working copy is created, you can use the normal ``vlt up`` and ``vlt ci`` commands.

## Specifying CRX Host/Port

The CRX host and port can be specified on the command line with:
mvn -Dcrx.host=otherhost -Dcrx.port=5502 <goals>


