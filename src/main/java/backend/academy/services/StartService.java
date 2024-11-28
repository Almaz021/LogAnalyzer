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
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@RequiredArgsConstructor
public class StartService {
    private final String[] args;
    private final PrintWriter printWriter;
    private DataProcessorService dataProcessorService;
    String path = "";
    LocalDate from = null;
    LocalDate to = null;
    String format = "";
    LogReader reader;
    Stream<String> lines;
    FilterService filterService = new FilterService();
    LogParser parser = new LogParser();
    String additionalFilter = "";

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
    }

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

    private void setReader() {
        if (path.contains("http") || path.contains("https")) {
            reader = new URLReader();
        } else {
            reader = new FileReader(dataProcessorService);
        }
    }

    private void readLogs() {
        lines.forEach(o -> {
            LogRecord logRecord = parser.parse(o);

            if (!isValid(logRecord)) {
                printWriter.println("NOT ACCEPTED");
                return;
            }

            update(logRecord);
        });
    }

    private boolean isValid(LogRecord logRecord) {
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

    public void update(LogRecord logRecord) {
        dataProcessorService.updateReport(logRecord);
    }
}
