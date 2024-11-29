package backend.academy.services;

import backend.academy.entities.LogRecord;
import backend.academy.entities.LogReport;
import backend.academy.settings.Settings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

/**
 * Processes log data to update the {@link LogReport}.
 */
@RequiredArgsConstructor
public class DataProcessorService {
    private final LogReport logReport;

    /**
     * Adds a file name to the log report.
     */
    public void addFileName(String fileName) {
        logReport.files().add(fileName);
    }

    /**
     * Updates the log report with data from a {@link LogRecord}.
     */
    public void updateReport(LogRecord logRecord) {
        updateDate(logRecord);
        updateRequestCount();
        updateSumAndAllRequestSize(logRecord);
        updateResourcesCount(logRecord);
        updateRequestStatusCount(logRecord);
        updateRequestTypeCount(logRecord);
    }

    /**
     * Updates the report's start and end dates based on the log record.
     */
    private void updateDate(LogRecord logRecord) {
        LocalDate timeLocal = logRecord.timeLocal();
        if (logReport.startDate() == null || logReport.startDate().isAfter(timeLocal)) {
            logReport.startDate(timeLocal);
        }
        if (logReport.endDate() == null || logReport.endDate().isBefore(timeLocal)) {
            logReport.endDate(timeLocal);
        }
    }

    /**
     * Increments the total request count in the log report.
     */
    private void updateRequestCount() {
        logReport.requestCount(logReport.requestCount() + 1);
    }

    /**
     * Updates the sum and list of all request sizes in the log report.
     */
    private void updateSumAndAllRequestSize(LogRecord logRecord) {
        logReport.sumRequestSize(logReport.sumRequestSize().add(logRecord.bodyBytesSent()));
        logReport.allRequestSize().add(logRecord.bodyBytesSent());
    }

    /**
     * Updates the resource usage statistics in the log report.
     */
    @SuppressFBWarnings("CLI_CONSTANT_LIST_INDEX")
    private void updateResourcesCount(LogRecord logRecord) {
        logReport.resourcesCount().merge(
            logRecord.request()[Settings.ONE],
            Settings.ONE,
            Integer::sum
        );
    }

    /**
     * Updates the count of HTTP status codes in the log report.
     */
    private void updateRequestStatusCount(LogRecord logRecord) {
        logReport.requestStatusCount().merge(
            String.valueOf(logRecord.status()),
            Settings.ONE,
            Integer::sum
        );
    }

    /**
     * Updates the count of HTTP request types in the log report.
     */
    private void updateRequestTypeCount(LogRecord logRecord) {
        logReport.requestTypeCount().merge(
            logRecord.request()[Settings.ZERO],
            Settings.ONE,
            Integer::sum
        );
    }
}
