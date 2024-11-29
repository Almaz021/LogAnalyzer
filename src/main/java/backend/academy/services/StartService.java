package backend.academy.services;

import backend.academy.entities.LogRecord;
import backend.academy.entities.LogReport;
import backend.academy.interfaces.FileWriter;
import backend.academy.interfaces.LogReader;
import backend.academy.parsers.LogParser;
import backend.academy.readers.FileReader;
import backend.academy.readers.URLReader;
import backend.academy.settings.Settings;
import backend.academy.writers.AdocFileWriter;
import backend.academy.writers.MarkdownFileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * This service reads log files (either from a file or a URL), filters them based on the specified criteria,
 * processes the logs to generate a report, and then writes the report in either Markdown or AsciiDoc format.
 */
@Log4j2
@RequiredArgsConstructor
public class StartService {
    private final String[] args;
    private final PrintWriter printWriter;
    private DataProcessorService dataProcessorService;
    private String path = "";
    private LocalDate from = null;
    private LocalDate to = null;
    private String additionalFilter = "";
    private String format = "";
    private LogReader reader;
    private Stream<String> lines;

    /**
     * This method parses command-line arguments, sets up the log reader based on the file or URL,
     * reads the logs, applies filters, processes the logs, and writes the resulting report to a file.
     */
    public void start() throws IOException {

        parseArgs();
        LogReport report = new LogReport();
        dataProcessorService = new DataProcessorService(report);
        setReader();

        lines = reader.read(path);

        readLogs();

        FileWriter writer;
        if ("markdown".equals(format)) {
            writer = new MarkdownFileWriter();
            writer.writeFile(Settings.RESULT_MARKDOWN_PATH, report);
        } else if ("adoc".equals(format)) {
            writer = new AdocFileWriter();
            writer.writeFile(Settings.RESULT_ADOC_PATH, report);
        } else {
            writer = new MarkdownFileWriter();
            writer.writeFile(Settings.RESULT_MARKDOWN_PATH, report);
        }
        log.info("Saved to: %s".formatted(Settings.RESULT_MARKDOWN_PATH));
    }

    /**
     * Parses command-line arguments to configure the log processing behavior.
     */
    private void parseArgs() {
        for (int i = 0; i < args.length; i = i + 2) {
            switch (args[i]) {
                case "--path" -> path = args[i + 1];
                case "--from" -> from = LocalDate.parse(args[i + 1], ISO_LOCAL_DATE);
                case "--to" -> to = LocalDate.parse(args[i + 1], ISO_LOCAL_DATE);
                case "--format" -> format = args[i + 1];
                case "--filter-value" -> additionalFilter = args[i + 1];
                default -> throw new IllegalArgumentException("Unknown option: " + args[i]);
            }
        }
    }

    /**
     * Sets the log reader based on the provided path (file or URL).
     */
    private void setReader() {
        if (path.contains("http") || path.contains("https")) {
            reader = new URLReader();
        } else {
            reader = new FileReader(dataProcessorService);
        }
    }

    /**
     * Reads and processes each log line.
     * <p>
     * Each line is parsed into a {@link LogRecord}, validated against filters, and then processed.
     */
    private void readLogs() {
        LogParser parser = new LogParser();
        lines.forEach(o -> {
            LogRecord logRecord = parser.parse(o);

            if (!isValid(logRecord)) {
                printWriter.println("NOT ACCEPTED");
                return;
            }

            update(logRecord);
        });
    }

    /**
     * Validates a log record based on the specified filters (date range and value).
     */
    private boolean isValid(LogRecord logRecord) {
        FilterService filterService = new FilterService();
        boolean isFilterValid = "".equals(additionalFilter) || filterService.filterByValue(logRecord, additionalFilter);
        if (!isFilterValid) {
            return false;
        }
        if (from != null || to != null) {
            return from != null && to != null
                ? filterService.filterByDate(logRecord, from, to)
                : filterService.filterByDate(logRecord, from != null ? from : to, from != null ? "from" : "to");
        }
        return true;
    }

    /**
     * Updates the log report with data from a valid log record.
     */
    private void update(LogRecord logRecord) {
        dataProcessorService.updateReport(logRecord);
    }
}
