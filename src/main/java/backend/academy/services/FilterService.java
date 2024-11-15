package backend.academy.services;

import backend.academy.entities.LogRecord;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilterService {

    public List<LogRecord> filterByDate(List<LogRecord> records, LocalDate from, LocalDate to) {
        List<LogRecord> reportCopy = new ArrayList<>(List.copyOf(records));

        reportCopy.removeIf(logRecord -> logRecord.timeLocal().isBefore(from) || logRecord.timeLocal().isAfter(to));

        return reportCopy;
    }
}

