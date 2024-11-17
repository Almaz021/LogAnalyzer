package backend.academy;

import java.io.PrintWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainInterface {
    @Getter private String currMessage;
    private final PrintWriter writer;

    private void printMessage(String msg) {
        writer.println(msg);
    }
}
