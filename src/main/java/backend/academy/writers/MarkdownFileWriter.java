package backend.academy.writers;

import backend.academy.StatusCodeLookup;
import backend.academy.entities.LogReport;
import backend.academy.interfaces.FileWriter;
import backend.academy.settings.ConstantStrings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A writer class responsible for generating a Markdown report from a LogReport.
 */
public class MarkdownFileWriter implements FileWriter {

    /**
     * Writes the formatted Markdown content to a specified file.
     */
    @Override
    @SuppressFBWarnings({"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "PATH_TRAVERSAL_IN",
        "PRMC_POSSIBLY_REDUNDANT_METHOD_CALLS"})
    public void writeFile(String fileName, LogReport report) {
        if (report == null) {
            throw new IllegalArgumentException("LogReport cannot be null");
        }

        String result = generateMarkdownContent(report);

        Path path = Paths.get(fileName).normalize();
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, result);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + fileName, e);
        }
    }

    /**
     * Generates the Markdown content for the given LogReport.
     */
    private String generateMarkdownContent(LogReport report) {
        StringBuilder builder = new StringBuilder();

        builder.append(generateGeneralInfoSection(report));

        builder.append(generateResourceSection(report));

        builder.append(generateResponseCodesSection(report));

        builder.append(generateRequestTypesSection(report));

        return builder.toString();
    }

    /**
     * Generates the general information section of the Markdown report.
     */
    private String generateGeneralInfoSection(LogReport report) {
        StringBuilder infoSection = new StringBuilder();
        infoSection.append(ConstantStrings.HEADER_MD.formatted(
            ConstantStrings.GENERAL_INFO_HEADER.formatted(ConstantStrings.NEW_LINE)));
        infoSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.METRIC_TABLE_HEADER, ConstantStrings.NEW_LINE));
        infoSection.append(ConstantStrings.TABLE_ALIGNMENT_MD);
        infoSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.FILES_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.files()));
        infoSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.START_DATE_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.startDate()));
        infoSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.END_DATE_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.endDate()));
        infoSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.REQUEST_COUNT_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.requestCount()));
        infoSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.AVG_RESPONSE_SIZE_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.getAverageRequestSize()));
        infoSection.append(ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.PERCENTILE_RESPONSE_SIZE_METRIC,
                ConstantStrings.NEW_LINE)
            .formatted(report.getPercentile()));
        return infoSection.toString();
    }

    /**
     * Generates the resource section of the Markdown report.
     */
    private String generateResourceSection(LogReport report) {
        StringBuilder resourceSection = new StringBuilder();
        resourceSection.append(
            ConstantStrings.HEADER_MD.formatted(ConstantStrings.RESOURCE_HEADER.formatted(ConstantStrings.NEW_LINE)));
        resourceSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.RESOURCE_TABLE_HEADER, ConstantStrings.NEW_LINE));
        resourceSection.append(ConstantStrings.TABLE_ALIGNMENT_MD);
        for (String key : report.resourcesCount().keySet()) {
            resourceSection.append(
                ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.TABLE_ROW_STRING_INT, ConstantStrings.NEW_LINE)
                    .formatted(key, report.resourcesCount().get(key)));
        }
        return resourceSection.toString();
    }

    /**
     * Generates the response codes section of the Markdown report.
     */
    private String generateResponseCodesSection(LogReport report) {
        StringBuilder responseCodesSection = new StringBuilder();
        responseCodesSection.append(
            ConstantStrings.HEADER_MD.formatted(
                ConstantStrings.RESPONSE_CODES_HEADER.formatted(ConstantStrings.NEW_LINE)));
        responseCodesSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.RESPONSE_CODES_TABLE_HEADER,
                ConstantStrings.NEW_LINE));
        responseCodesSection.append(ConstantStrings.TABLE_ALIGNMENT_MD_DETAILED);
        for (String status : report.requestStatusCount().keySet()) {
            responseCodesSection.append(
                ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.RESPONSE_CODES_TABLE_ROW,
                    ConstantStrings.NEW_LINE).formatted(
                    status,
                    StatusCodeLookup.getDescription(Integer.parseInt(status)),
                    report.requestStatusCount().get(status)
                ));
        }
        return responseCodesSection.toString();
    }

    /**
     * Generates the request types section of the Markdown report.
     */
    private String generateRequestTypesSection(LogReport report) {
        StringBuilder requestTypesSection = new StringBuilder();
        requestTypesSection.append(
            ConstantStrings.HEADER_MD.formatted(
                ConstantStrings.REQUEST_TYPES_HEADER.formatted(ConstantStrings.NEW_LINE)));
        requestTypesSection.append(
            ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.REQUEST_TYPES_TABLE_HEADER,
                ConstantStrings.NEW_LINE));
        requestTypesSection.append(ConstantStrings.TABLE_ALIGNMENT_MD);
        for (String key : report.requestTypeCount().keySet()) {
            requestTypesSection.append(
                ConstantStrings.TABLE_ROW_MD.formatted(ConstantStrings.TABLE_ROW_STRING_INT, ConstantStrings.NEW_LINE)
                    .formatted(key, report.requestTypeCount().get(key)));
        }
        return requestTypesSection.toString();
    }
}
