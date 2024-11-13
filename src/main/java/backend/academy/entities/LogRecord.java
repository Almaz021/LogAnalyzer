package backend.academy.entities;

import java.time.LocalDate;

public record LogRecord(String remoteAddr,
                        String remoteUser,
                        LocalDate timeLocal,
                        String request,
                        Integer status,
                        Integer bodyBytesSent,
                        String httpReferer,
                        String httpUserAgent) {
}
