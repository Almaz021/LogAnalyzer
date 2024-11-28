package backend.academy.services;

import backend.academy.entities.LogRecord;
import backend.academy.entities.LogReport;
import backend.academy.settings.Settings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataProcessorService {
    private final LogReport logReport;

    public void addFileName(String fileName) {
        logReport.files().add(fileName);
    }

    public void updateReport(LogRecord logRecord) {
        updateDate(logRecord);
        updateRequestCount();
        updateSumAndAllRequestSize(logRecord);
        updateResourcesCount(logRecord);
        updateRequestStatusCount(logRecord);
        updateRequestTypeCount(logRecord);
    }

    private void updateDate(LogRecord logRecord) {
        LocalDate timeLocal = logRecord.timeLocal();
        if (logReport.startDate() == null || logReport.startDate().isAfter(timeLocal)) {
            logReport.startDate(timeLocal);
        }
        if (logReport.endDate() == null || logReport.endDate().isBefore(timeLocal)) {
            logReport.endDate(timeLocal);
        }
    }

    private void updateRequestCount() {
        logReport.requestCount(logReport.requestCount() + 1);
    }

    private void updateSumAndAllRequestSize(LogRecord logRecord) {
        logReport.sumRequestSize(logReport.sumRequestSize().add(logRecord.bodyBytesSent()));
        logReport.allRequestSize().add(logRecord.bodyBytesSent());
    }

    @SuppressFBWarnings("CLI_CONSTANT_LIST_INDEX")
    private void updateResourcesCount(LogRecord logRecord) {
        logReport.resourcesCount().merge(
            logRecord.request()[Settings.ONE],
            Settings.ONE,
            Integer::sum
        );
    }

    private void updateRequestStatusCount(LogRecord logRecord) {
        logReport.requestStatusCount().merge(
            String.valueOf(logRecord.status()),
            Settings.ONE,
            Integer::sum
        );
    }

    private void updateRequestTypeCount(LogRecord logRecord) {
        logReport.requestTypeCount().merge(
            logRecord.request()[Settings.ZERO],
            Settings.ONE,
            Integer::sum
        );
    }
}
