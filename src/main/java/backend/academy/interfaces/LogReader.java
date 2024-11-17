package backend.academy.interfaces;

import java.io.IOException;
import java.util.stream.Stream;

public interface LogReader {
    Stream<String> read(String path) throws IOException;
}
