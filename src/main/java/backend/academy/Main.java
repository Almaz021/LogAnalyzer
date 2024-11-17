package backend.academy;

import backend.academy.services.ExceptionHandlerService;
import backend.academy.services.StartService;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            StartService startService =
                new StartService(args, new PrintWriter(System.out, true, StandardCharsets.UTF_8));
            startService.start();
        } catch (Exception e) {
            ExceptionHandlerService.handleException(e);
            System.exit(1);
        }
    }
}
