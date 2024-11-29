package backend.academy.services;

import backend.academy.entities.LogRecord;
import java.time.LocalDate;

/**
 * Provides filtering capabilities for {@link LogRecord} objects.
 * <p>
 * This service supports filtering log records by date or values.
 */
public class FilterService {

    /**
     * Filters log records by a date range.
     */
    public boolean filterByDate(LogRecord logRecord, LocalDate from, LocalDate to) {
        return ((logRecord.timeLocal().isAfter(from) || logRecord.timeLocal().isEqual(from))
            && logRecord.timeLocal().isBefore(to) || logRecord.timeLocal().isEqual(to));
    }

    /**
     * Filters log records by a specific date in either "from" or "to" mode.
     */
    public boolean filterByDate(LogRecord logRecord, LocalDate date, String s) {
        if ("from".equals(s)) {
            return logRecord.timeLocal().isAfter(date) || logRecord.timeLocal().isEqual(date);
        } else {
            return logRecord.timeLocal().isBefore(date) || logRecord.timeLocal().isEqual(date);
        }
    }

    /**
     * Filters log records by the presence of a specific value in the User-Agent field.
     */
    public boolean filterByValue(LogRecord logRecord, String s) {
        return logRecord.httpUserAgent().contains(s);
    }
}
