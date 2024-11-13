package backend.academy.services;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class ExceptionHandlerService {

    public void handleException(Exception e) {
        log.error(e.getMessage());
    }
}
