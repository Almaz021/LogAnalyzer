package backend.academy.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LogRecord(String remoteAddr,
                        String remoteUser,
                        LocalDate timeLocal,
                        String[] request,
                        Integer status,
                        BigDecimal bodyBytesSent,
                        String httpReferer,
                        String httpUserAgent) {
}
