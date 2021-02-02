package com.github.AkMo03;

import java.util.Locale;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * Creates a website to go to SonarCloud dashboard of checkstyle.
 *
 */
@Mojo(
        name = "SonarCloud Report",
        defaultPhase = LifecyclePhase.SITE,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        requiresProject = true,
        threadSafe = true
)
public class SonarCloudReport extends AbstractMavenReport {

    /**
     * This report will generate SonarCloud-Report.html when
     * invoked in a project with `mvn site`.
     *
     * @return SonarCloud-Report
     */
    public String getOutputName() {
        return "SonarCloud-Report";
    }

    /**
     * Name of the report when listed in the project-reports.html page of a project.
     *
     * @param locale For language specific.
     * @return SonarCloud report.
     */
    public String getName(Locale locale) {
        return "SonarCloud Report";
    }

    /**
     * Description of the report when listed in the project-reports.html page of a project.
     *
     * @param locale For language specific.
     * @return Description of report.
     */
    public String getDescription(Locale locale) {
        return "Dashboard for code quality management";
    }

    /**
     * Practical reference to the Maven project
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {

        /* Link to Report */
        String sonarDashBoard = "https://sonarcloud.io/dashboard?id=org.checkstyle%3Acheckstyle";

        /* Get the logger */
        Log logger = getLog();

        /* Some info */
        logger.info("Generating " + getOutputName() + ".html"
                + " for " + project.getName() + " " + project.getVersion());

        /**
         * Get the Maven Doxia Sink, which will be used to generate the
         * various elements of the document.
         */
        Sink mainSink = getSink();
        if (mainSink == null) {
            throw new MavenReportException("Could not get the Doxia sink");
        }

        /* Page title */
        mainSink.head();
        mainSink.title();
        mainSink.text("SonarQube Report");
        mainSink.title_();
        mainSink.head_();

        mainSink.body();

        /* Heading 1 */
        mainSink.section1();
        mainSink.sectionTitle1();
        mainSink.text("SonarQube Report");
        mainSink.sectionTitle1_();

        /* Content */
        mainSink.paragraph();
        mainSink.text("The reports are present at Sonarcloud.io.  ");
        mainSink.link(sonarDashBoard);
        mainSink.text("Click Here");
        mainSink.link_();
        mainSink.text("to see the reports");
        mainSink.paragraph_();

        // Close
        mainSink.section1_();
        mainSink.body_();

    }

}