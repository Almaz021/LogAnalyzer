package backend.academy.writers;

import backend.academy.StatusCodeLookup;
import backend.academy.entities.LogReport;
import backend.academy.interfaces.FileWriter;
import backend.academy.settings.ConstantStrings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class AdocFileWriter implements FileWriter {

    @Override
    @SuppressFBWarnings({"PATH_TRAVERSAL_OUT", "IOI_USE_OF_FILE_STREAM_CONSTRUCTORS"})
    public void writeFile(String fileName, LogReport report) {
        String result = generateAdocContent(report);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateAdocContent(LogReport report) {
        StringBuilder builder = new StringBuilder();

        builder.append(generateGeneralInfoSection(report));

        builder.append(generateResourceSection(report));

        builder.append(generateResponseCodesSection(report));

        builder.append(generateRequestTypesSection(report));

        return builder.toString();
    }

    private String generateGeneralInfoSection(LogReport report) {
        StringBuilder infoSection = new StringBuilder();
        infoSection.append(ConstantStrings.HEADER_ADOC.formatted(
            ConstantStrings.GENERAL_INFO_HEADER.formatted(ConstantStrings.NEW_LINE)));
        infoSection.append(ConstantStrings.METRIC_TABLE_HEADER_ADOC.formatted(ConstantStrings.NEW_LINE,
            ConstantStrings.NEW_LINE, ConstantStrings.NEW_LINE));
        infoSection.append(
            ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.FILES_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.files()));
        infoSection.append(
            ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.START_DATE_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.startDate()));
        infoSection.append(
            ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.END_DATE_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.endDate()));
        infoSection.append(
            ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.REQUEST_COUNT_METRIC, ConstantStrings.NEW_LINE)
                .formatted(report.requestCount()));
        infoSection.append(ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.AVG_RESPONSE_SIZE_METRIC,
                ConstantStrings.NEW_LINE)
            .formatted(report.getAverageRequestSize()));
        infoSection.append(ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.PERCENTILE_RESPONSE_SIZE_METRIC,
                ConstantStrings.NEW_LINE)
            .formatted(report.getPercentile()));
        infoSection.append(ConstantStrings.TABLE_ADOC.formatted(ConstantStrings.NEW_LINE));
        return infoSection.toString();
    }

    private String generateResourceSection(LogReport report) {
        StringBuilder resourceSection = new StringBuilder();
        resourceSection.append(ConstantStrings.HEADER_ADOC.formatted(
            ConstantStrings.RESOURCE_HEADER.formatted(ConstantStrings.NEW_LINE)));
        resourceSection.append(ConstantStrings.RESOURCE_TABLE_HEADER_ADOC.formatted(ConstantStrings.NEW_LINE,
            ConstantStrings.NEW_LINE, ConstantStrings.NEW_LINE));
        for (String key : report.resourcesCount().keySet()) {
            resourceSection.append(ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.TABLE_ROW_STRING_INT,
                    ConstantStrings.NEW_LINE)
                .formatted(key, report.resourcesCount().get(key)));
        }
        resourceSection.append(ConstantStrings.TABLE_ADOC.formatted(ConstantStrings.NEW_LINE));
        return resourceSection.toString();
    }

    private String generateResponseCodesSection(LogReport report) {
        StringBuilder responseCodesSection = new StringBuilder();
        responseCodesSection.append(
            ConstantStrings.HEADER_ADOC.formatted(
                ConstantStrings.RESPONSE_CODES_HEADER.formatted(ConstantStrings.NEW_LINE)));
        responseCodesSection.append(
            ConstantStrings.RESPONSE_CODES_TABLE_HEADER_ADOC.formatted(ConstantStrings.NEW_LINE,
                ConstantStrings.NEW_LINE, ConstantStrings.NEW_LINE));
        for (String status : report.requestStatusCount().keySet()) {
            responseCodesSection.append(
                ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.RESPONSE_CODES_TABLE_ROW,
                    ConstantStrings.NEW_LINE).formatted(
                    status,
                    StatusCodeLookup.getDescription(Integer.parseInt(status)),
                    report.requestStatusCount().get(status)
                ));
        }
        responseCodesSection.append(ConstantStrings.TABLE_ADOC.formatted(ConstantStrings.NEW_LINE));
        return responseCodesSection.toString();
    }

    private String generateRequestTypesSection(LogReport report) {
        StringBuilder requestTypesSection = new StringBuilder();
        requestTypesSection.append(
            ConstantStrings.HEADER_ADOC.formatted(
                ConstantStrings.REQUEST_TYPES_HEADER.formatted(ConstantStrings.NEW_LINE)));
        requestTypesSection.append(
            ConstantStrings.REQUEST_TYPES_TABLE_HEADER_ADOC.formatted(ConstantStrings.NEW_LINE,
                ConstantStrings.NEW_LINE, ConstantStrings.NEW_LINE));
        for (String key : report.requestTypeCount().keySet()) {
            requestTypesSection.append(ConstantStrings.TABLE_ROW_ADOC.formatted(ConstantStrings.TABLE_ROW_STRING_INT,
                    ConstantStrings.NEW_LINE)
                .formatted(key, report.requestTypeCount().get(key)));
        }
        requestTypesSection.append(ConstantStrings.TABLE_ADOC.formatted(ConstantStrings.NEW_LINE));
        return requestTypesSection.toString();
    }
}
