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
    private String result = "";

    @Override
    @SuppressFBWarnings({"PATH_TRAVERSAL_OUT", "IOI_USE_OF_FILE_STREAM_CONSTRUCTORS"})
    public void writeFile(String fileName, LogReport report) {
        generate(report);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generate(LogReport report) {
        result += ConstantStrings.FIRST_DELIMITER + ConstantStrings.FIRST_DELIMITER + ConstantStrings.MAIN_INFO
            + ConstantStrings.NEW_LINE;
        result += ConstantStrings.ADOC_TABLE_START + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.FIRST_DELIMITER + ConstantStrings.FIRST_DELIMITER
            + ConstantStrings.FIRST_DELIMITER + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.METRIC + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.VALUE + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.FILES + ConstantStrings.SECOND_DELIMITER
            + report.files() + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.START_DATE + ConstantStrings.SECOND_DELIMITER
            + report.startDate() + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.END_DATE + ConstantStrings.SECOND_DELIMITER
            + report.endDate() + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.REQUEST_COUNT + ConstantStrings.SECOND_DELIMITER
            + report.requestCount() + ConstantStrings.NEW_LINE;
        result +=
            ConstantStrings.SECOND_DELIMITER + ConstantStrings.AVG_RESPONSE_SIZE + ConstantStrings.SECOND_DELIMITER
                + report.getAverageRequestSize() + ConstantStrings.NEW_LINE;
        result +=
            ConstantStrings.SECOND_DELIMITER + ConstantStrings.PERCENTILE_95_TEXT + ConstantStrings.SECOND_DELIMITER
                + report.getPercentile() + ConstantStrings.NEW_LINE;
        result += ConstantStrings.HEADER_SEPARATOR + ConstantStrings.NEW_LINE;

        result +=
            ConstantStrings.FIRST_DELIMITER + ConstantStrings.FIRST_DELIMITER + ConstantStrings.REQUESTED_RESOURCES
                + ConstantStrings.NEW_LINE;
        result += ConstantStrings.ADOC_TABLE_START + ConstantStrings.NEW_LINE;
        result += ConstantStrings.HEADER_SEPARATOR + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.RESOURCE_COLUMN + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.COUNT_COLUMN + ConstantStrings.NEW_LINE;
        for (String key : report.resourcesCount().keySet()) {
            result += ConstantStrings.SECOND_DELIMITER + key + ConstantStrings.SECOND_DELIMITER
                + report.resourcesCount().get(key) + ConstantStrings.NEW_LINE;
        }
        result += ConstantStrings.HEADER_SEPARATOR + ConstantStrings.NEW_LINE;

        result += ConstantStrings.FIRST_DELIMITER + ConstantStrings.FIRST_DELIMITER + ConstantStrings.RESPONSE_CODES
            + ConstantStrings.NEW_LINE;
        result += ConstantStrings.ADOC_TABLE_START + ConstantStrings.NEW_LINE;
        result += ConstantStrings.HEADER_SEPARATOR + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.CODE_COLUMN + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.NAME_COLUMN + ConstantStrings.SECOND_DELIMITER + ConstantStrings.COUNT_COLUMN
            + ConstantStrings.NEW_LINE;
        for (String status : report.requestStatusCount().keySet()) {
            result += ConstantStrings.SECOND_DELIMITER + status + ConstantStrings.SECOND_DELIMITER
                + StatusCodeLookup.getDescription(Integer.parseInt(status)) + ConstantStrings.SECOND_DELIMITER
                + report.requestStatusCount().get(status) + ConstantStrings.NEW_LINE;
        }
        result += ConstantStrings.HEADER_SEPARATOR + ConstantStrings.NEW_LINE;
    }
}
