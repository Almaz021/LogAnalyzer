package backend.academy.services;

import backend.academy.entities.LogRecord;
import backend.academy.entities.LogReport;
import backend.academy.settings.Settings;
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
    }

    private void updateDate(LogRecord logRecord) {
        if (logReport.startDate() == null && logReport.endDate() == null) {
            logReport.startDate(logRecord.timeLocal());
            logReport.endDate(logRecord.timeLocal());
        }
        if (logReport.startDate() != null && logReport.startDate().isAfter(logRecord.timeLocal())) {
            logReport.startDate(logRecord.timeLocal());
        }
        if (logReport.endDate() != null && logReport.endDate().isBefore(logRecord.timeLocal())) {
            logReport.endDate(logRecord.timeLocal());
        }
    }

    private void updateRequestCount() {
        logReport.requestCount(logReport.requestCount() + 1);
    }

    private void updateSumAndAllRequestSize(LogRecord logRecord) {
        logReport.sumRequestSize(logReport.sumRequestSize().add(logRecord.bodyBytesSent()));
        logReport.allRequestSize().add(logRecord.bodyBytesSent());
    }

    private void updateResourcesCount(LogRecord logRecord) {
        logReport.resourcesCount().put(logRecord.request()[Settings.ZERO],
            logReport.resourcesCount().getOrDefault(logRecord.request()[Settings.ONE], 0) + 1);
    }

    private void updateRequestStatusCount(LogRecord logRecord) {
        logReport.requestStatusCount().put(String.valueOf(logRecord.status()),
            logReport.requestStatusCount().getOrDefault(String.valueOf(logRecord.status()), 0) + 1);
    }
}
