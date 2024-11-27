package backend.academy.services;

import backend.academy.entities.LogRecord;
import java.time.LocalDate;

public class FilterService {

    public boolean filterByDate(LogRecord logRecord, LocalDate from, LocalDate to) {
        return ((logRecord.timeLocal().isAfter(from) || logRecord.timeLocal().isEqual(from))
            && logRecord.timeLocal().isBefore(to) || logRecord.timeLocal().isEqual(to));
    }

    public boolean filterByDate(LogRecord logRecord, LocalDate date, String s) {
        if ("from".equals(s)) {
            return logRecord.timeLocal().isAfter(date) || logRecord.timeLocal().isEqual(date);
        } else {
            return logRecord.timeLocal().isBefore(date) || logRecord.timeLocal().isEqual(date);
        }
    }

    public boolean filterByValue(LogRecord logRecord, String s) {
        return logRecord.httpUserAgent().contains(s);
    }
}
