package ru.bootdev;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

@Mojo(name = "xlsReport", threadSafe = true)
public class TestngResultsToXlsMojo extends AbstractMojo {

    @Parameter(
            name = "sourceXml",
            property = "xlsReport.sourceXml",
            defaultValue = "test-output/testng-results.xml",
            required = false
    )
    private String sourceXml;

    @Parameter(
            name = "excludedMethods",
            property = "xlsReport.excludedMethods",
            defaultValue = "",
            required = false
    )
    private String excludedMethods;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File xmlFile = new File(String.format("%s/%s", project.getBasedir(), sourceXml));
        getLog().info("******* START: TestNG Results XML to XLS Maven Plugin *******");
        try {
            getLog().info(String.format(">>> Parsing XML results file [%s]", xmlFile.getPath()));
            TestngResultsXmlParser testResultsXmlParser = new TestngResultsXmlParser(xmlFile);
            testResultsXmlParser.parse();
            getLog().info(">>> Generating XLSX files");
            XlsBuilder xlsBuilder = new XlsBuilder(testResultsXmlParser.getData(), excludedMethods);
            xlsBuilder.generateXls();
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        getLog().info("******* END: TestNG Results XML to XLS Maven Plugin *******");
    }
}
