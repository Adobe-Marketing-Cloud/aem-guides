# Projects and Tasks Guide

This is the code companion to a 2 part series on HelpX:

1. [Developing Projects in AEM - Part 1](https://helpx.adobe.com/experience-manager/kt/platform-repository/using/projects-part1-tutorial-develop.html)
2. [Developing Projects in AEM - Part 2](https://helpx.adobe.com/experience-manager/kt/platform-repository/using/projects-part2-tutorial-develop.html)

This will install the completed code for both parts 1 and 2.

## Building

This project uses Maven for building. Common commands:

From the root directory, run ``mvn -PautoInstallPackage clean install`` to build the bundle and content package and install to a CQ instance.

## Using with AEM Developer Tools for Eclipse

To use this project with the AEM Developer Tools for Eclipse, import the generated Maven projects via the Import:Maven:Existing Maven Projects wizard. Then enable the Content Package facet on the _content_ project by right-clicking on the project, then select Configure, then Convert to Content Package... In the resulting dialog, select _src/main/content_ as the Content Sync Root.

## Using with VLT

To use vlt with this project, first build and install the package to your local CQ instance as described above. Then cd to `content/src/main/content/jcr_root` and run

    vlt --credentials admin:admin checkout -f ../META-INF/vault/filter.xml --force http://localhost:4502/crx

Once the working copy is created, you can use the normal ``vlt up`` and ``vlt ci`` commands.

## Specifying CRX Host/Port

The CRX host and port can be specified on the command line with:
mvn -Dcrx.host=otherhost -Dcrx.port=5502 <goals>


