package backend.academy.parsers;

import backend.academy.entities.LogRecord;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@RequiredArgsConstructor
public class LogParser {
    private final static int REQUEST_TYPE_INDEX = 3;
    private final static int REQUEST_RESOURCE_INDEX = 4;
    private final static int REQUEST_HTTP_INDEX = 5;
    private final static int STATUS_INDEX = 6;
    private final static int BODY_BYTES_SENT_INDEX = 7;
    private final static int HTTP_REFERER_INDEX = 8;
    private final static int HTTP_USER_AGENT_START_INDEX = 9;

    public LogRecord parse(String line) {
        String[] firstSplit = line.split(" - ");
        String[] secondSplit = firstSplit[1].split(" ");

        String remoteAddr = firstSplit[0];

        String remoteUser = secondSplit[0];

        LocalDate timeLocal = parseTime(secondSplit);

        String requestType = secondSplit[REQUEST_TYPE_INDEX];
        String requestResource = secondSplit[REQUEST_RESOURCE_INDEX];
        String requestHTTP = secondSplit[REQUEST_HTTP_INDEX];
        String[] request = {requestType, requestResource, requestHTTP};

        Integer status = Integer.valueOf(secondSplit[STATUS_INDEX]);
        BigDecimal bodyBytesSent = new BigDecimal(secondSplit[BODY_BYTES_SENT_INDEX]);

        String httpReferer = secondSplit[HTTP_REFERER_INDEX];

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

    @SuppressFBWarnings("CLI_CONSTANT_LIST_INDEX")
    public LocalDate parseTime(String[] line) {
        String rawTimeLocal = line[1].split(":")[0].substring(1);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(rawTimeLocal, inputFormatter);
        String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return LocalDate.parse(formattedDate, ISO_LOCAL_DATE);
    }

    public String parseHttpUserAgent(String[] line) {
        StringBuilder httpUserAgentBuilder = new StringBuilder(line[HTTP_USER_AGENT_START_INDEX]);
        for (int i = HTTP_USER_AGENT_START_INDEX + 1; i < line.length; i++) {
            httpUserAgentBuilder.append(' ').append(line[i]);
        }
        return httpUserAgentBuilder.toString();
    }
}
