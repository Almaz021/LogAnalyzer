package backend.academy.services;

import backend.academy.entities.LogRecord;
import java.time.LocalDate;

public class FilterService {

    public boolean filterByDate(LogRecord logRecord, LocalDate from, LocalDate to) {
        return logRecord.timeLocal().isAfter(from) || logRecord.timeLocal().isBefore(to);
    }

    public boolean filterByDate(LogRecord logRecord, LocalDate date, String s) {
        if ("from".equals(s)) {
            return logRecord.timeLocal().isAfter(date);
        } else {
            return logRecord.timeLocal().isBefore(date);
        }
    }

    public boolean filterByValue(LogRecord logRecord, String s) {
        return logRecord.httpUserAgent().contains(s);
    }
}
