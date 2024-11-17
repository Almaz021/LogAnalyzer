package backend.academy.writers;

import backend.academy.StatusCodeLookup;
import backend.academy.entities.LogReport;
import backend.academy.interfaces.FileWriter;
import backend.academy.settings.ConstantStrings;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class MarkdownFileWriter implements FileWriter {
    private String result = "";

    @Override
    public void writeFile(String fileName, LogReport report) {
        // Null check for the report object and its methods
        if (report == null) {
            throw new IllegalArgumentException("LogReport cannot be null");
        }

        // Proceed to generate the file content
        generate(report);

        // Normalize the file path to avoid path traversal vulnerabilities
        Path path = Paths.get(fileName).normalize();  // Normalize path to prevent traversal

        // Ensure that the parent directories exist
        try {
            Files.createDirectories(path.getParent());  // Create directories if they don't exist
            Files.write(path, result.getBytes(StandardCharsets.UTF_8));  // Write content securely
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + fileName, e);
        }
    }

    private void generate(LogReport report) {
        // Section: General Information
        result += ConstantStrings.MARKDOWN_HASH + ConstantStrings.MAIN_INFO + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.METRIC + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.VALUE + ConstantStrings.SECOND_DELIMITER + ConstantStrings.NEW_LINE;
        result += ConstantStrings.TABLE_HEADER_SEPARATOR + ConstantStrings.NEW_LINE;

        // Null checks before accessing methods
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.FILES + ConstantStrings.SECOND_DELIMITER
            + (report.files() != null ? report.files() : ConstantStrings.NA) + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.START_DATE + ConstantStrings.SECOND_DELIMITER
            + (report.startDate() != null ? report.startDate() : ConstantStrings.NA) + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.END_DATE + ConstantStrings.SECOND_DELIMITER
            + (report.endDate() != null ? report.endDate() : ConstantStrings.NA) + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.REQUEST_COUNT + ConstantStrings.SECOND_DELIMITER
            + (report.requestCount() != null ? report.requestCount() : "0") + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.NEW_LINE;
        result +=
            ConstantStrings.SECOND_DELIMITER + ConstantStrings.AVG_RESPONSE_SIZE + ConstantStrings.SECOND_DELIMITER
                + (report.getAverageRequestSize() != null ? report.getAverageRequestSize() : ConstantStrings.NA)
                + ConstantStrings.SECOND_DELIMITER + ConstantStrings.NEW_LINE;
        result +=
            ConstantStrings.SECOND_DELIMITER + ConstantStrings.PERCENTILE_95_TEXT + ConstantStrings.SECOND_DELIMITER
                + (report.getPercentile() != null ? report.getPercentile() : ConstantStrings.NA)
                + ConstantStrings.SECOND_DELIMITER
                + ConstantStrings.NEW_LINE;

        // Section: Requested Resources
        result += ConstantStrings.MARKDOWN_HASH + ConstantStrings.REQUESTED_RESOURCES + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.RESOURCE_COLUMN + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.COUNT_COLUMN + ConstantStrings.SECOND_DELIMITER + ConstantStrings.NEW_LINE;
        result += ConstantStrings.TABLE_HEADER_SEPARATOR + ConstantStrings.NEW_LINE;

        // Null check for resources count
        for (Map.Entry<?, ?> entry : report.resourcesCount() != null ? report.resourcesCount().entrySet()
            : Map.of().entrySet()) {
            result += ConstantStrings.SECOND_DELIMITER + entry.getKey() + ConstantStrings.SECOND_DELIMITER
                + entry.getValue() + ConstantStrings.SECOND_DELIMITER + ConstantStrings.NEW_LINE;
        }

        // Section: Response Codes
        result += ConstantStrings.MARKDOWN_HASH + ConstantStrings.RESPONSE_CODES + ConstantStrings.NEW_LINE;
        result += ConstantStrings.SECOND_DELIMITER + ConstantStrings.CODE_COLUMN + ConstantStrings.SECOND_DELIMITER
            + ConstantStrings.NAME_COLUMN + ConstantStrings.SECOND_DELIMITER + ConstantStrings.COUNT_COLUMN
            + ConstantStrings.SECOND_DELIMITER + ConstantStrings.NEW_LINE;
        result += ConstantStrings.TABLE_ROW_SEPARATOR + ConstantStrings.NEW_LINE;

        // Null check for status codes count
        for (Map.Entry<?, ?> entry : report.requestStatusCount() != null ? report.requestStatusCount().entrySet()
            : Map.of().entrySet()) {
            result += ConstantStrings.SECOND_DELIMITER + entry.getKey() + ConstantStrings.SECOND_DELIMITER
                + StatusCodeLookup.getDescription(Integer.parseInt((String) entry.getKey()))
                + ConstantStrings.SECOND_DELIMITER
                + entry.getValue() + ConstantStrings.SECOND_DELIMITER + ConstantStrings.NEW_LINE;
        }
    }
}
