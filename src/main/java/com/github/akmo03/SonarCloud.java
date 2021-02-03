package com.github.akmo03;

import java.util.Locale;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * Creates a website to go to the SonarCloud dashboard.
 */
@Mojo(
        name = "SonarCloud Report",
        defaultPhase = LifecyclePhase.SITE,
        requiresDependencyResolution = ResolutionScope.RUNTIME,
        requiresProject = true,
        threadSafe = true
)
public class SonarCloud extends AbstractMavenReport {

    @Parameter( property = "sonar.report.url", alias = "sonar.report.url", required = false )
    private String sonarReportURL;

    /**
     * SonarCloud host url; property = "sonar.host.url", alias = "sonar.host.url" (default: http://localhost:9000).
     *
     * @since 1.0.2
     * @return sonarReportUrl parameter given by user.
     */
    protected String getSonarReportURL() {
        return sonarReportURL;
    }

    protected void setSonarReportURL(String sonarReportURL) {
        this.sonarReportURL = sonarReportURL;
    }

    @Parameter( property = "customGroupId", alias = "customGroupId", required = false)
    private String customGroupId;

    /**
     * Organization name.
     *
     * @since 1.0.2
     * @return customGroupId parameter given by user.
     */
    protected String getCustomGroupId() {
        return customGroupId;
    }

    protected void setCustomGroupId(String customGroupId) {
        this.customGroupId = customGroupId;
    }

    @Parameter( property = "customArtifactId", alias = "customArtifactId", required = false)
    private String customArtifactId;

    /**
     * Artifact name.
     *
     * @since 1.0.2
     * @return sonarReportUrl parameter given by user.
     */
    protected String getCustomArtifactId() {
        return customArtifactId;
    }

    protected void setCustomArtifactId(String customArtifactId) {
        this.customArtifactId = customArtifactId;
    }

    final StringBuilder sonarcloudLink = new StringBuilder("https://sonarcloud.io/dashboard?id=");

    /**
     * This report will generate SonarCloud-Report.html when
     * invoked in a project with `mvn site`.
     *
     * @return SonarCloud-Report.
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

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {

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
        String finalUrl = getProjectUrl();
        mainSink.paragraph();
        mainSink.text("The reports are present at Sonarcloud.io.  ");
        mainSink.link(finalUrl);
        mainSink.text("Click Here");
        mainSink.link_();
        mainSink.text("to see the reports");
        mainSink.paragraph_();
        mainSink.rawText("<script type='text/javascript'> window.location='" + finalUrl + "'</script>");

        // Close
        mainSink.section1_();
        mainSink.body_();

    }
    private String getProjectUrl() {
        if(sonarReportURL != null) {
            return sonarReportURL;
        }
        if(customGroupId == null &&
                customArtifactId != null) {
            return String.valueOf(sonarcloudLink
                    .append(getProject().getGroupId())
                    .append(":")
                    .append(customArtifactId));
        }
        if(customGroupId != null &&
                customArtifactId == null) {
            return String.valueOf(sonarcloudLink
                    .append(customGroupId)
                    .append(":")
                    .append(getProject().getArtifactId()));
        }
        if(customGroupId != null) {
            return String.valueOf(sonarcloudLink
                    .append(customGroupId)
                    .append(":")
                    .append(customArtifactId));
        }
        return String.valueOf(sonarcloudLink
                .append(getProject().getGroupId())
                .append(":")
                .append(getProject().getArtifactId()));
    }
}