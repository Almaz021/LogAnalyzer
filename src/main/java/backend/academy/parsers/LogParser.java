package backend.academy.parsers;

import backend.academy.entities.LogRecord;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@RequiredArgsConstructor
public class LogParser {
    private final static int REQUEST_FIRST_INDEX = 3;
    private final static int REQUEST_SECOND_INDEX = 4;
    private final static int REQUEST_THIRD_INDEX = 5;
    private final static int STATUS_INDEX = 6;
    private final static int BODY_BYTES_SENT_INDEX = 7;
    private final static int HTTP_REFERER_INDEX = 8;
    private final static int HTTP_USER_AGENT_START_INDEX = 9;

    public LogRecord parse(String line) {
        String[] firstSplit = line.split(" - ");
        String[] secondSplit = parseByIndex(firstSplit, 1).split(" ");

        String remoteAddr = parseByIndex(firstSplit, 0);

        String remoteUser = parseByIndex(secondSplit, 0);

        LocalDate timeLocal = parseTime(secondSplit);

        String requestType = parseByIndex(secondSplit, REQUEST_FIRST_INDEX);
        String requestResource = parseByIndex(secondSplit, REQUEST_SECOND_INDEX);
        String requestHTTP = parseByIndex(secondSplit, REQUEST_THIRD_INDEX);
        String[] request = {requestType, requestResource, requestHTTP};

        Integer status = Integer.valueOf(parseByIndex(secondSplit, STATUS_INDEX));
        BigDecimal bodyBytesSent = new BigDecimal(parseByIndex(secondSplit, BODY_BYTES_SENT_INDEX));

        String httpReferer = parseByIndex(secondSplit, HTTP_REFERER_INDEX);

        String httpUserAgent = parseHttpUserAgent(secondSplit);

        return new LogRecord(
            remoteAddr,
            remoteUser,
            timeLocal,
            request,
            status,
            bodyBytesSent,
            httpReferer,
            httpUserAgent);
    }

    public String parseByIndex(String[] line, int index) {
        return line[index];
    }

    public LocalDate parseTime(String[] line) {
        String rawTimeLocal = parseByIndex(line, 1).split(":")[0].substring(1);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(rawTimeLocal, inputFormatter);
        String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return LocalDate.parse(formattedDate, ISO_LOCAL_DATE);
    }

    public String parseHttpUserAgent(String[] line) {
        StringBuilder httpUserAgentBuilder = new StringBuilder();
        for (int i = HTTP_USER_AGENT_START_INDEX; i < line.length; i++) {
            if (i != HTTP_USER_AGENT_START_INDEX) {
                httpUserAgentBuilder.append(' ').append(parseByIndex(line, i));
            } else {
                httpUserAgentBuilder.append(parseByIndex(line, i));
            }
        }
        return httpUserAgentBuilder.toString();
    }
}
