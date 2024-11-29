package backend.academy.readers;

import backend.academy.interfaces.LogReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

/**
 * A URL reader implementation of {@link LogReader} for reading log data from a web resource.
 * <p>
 * This class sends an HTTP GET request to the specified URL and streams the response line by line.
 */
public class URLReader implements LogReader {

    @Override
    public Stream<String> read(String path) throws IOException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(path))
            .GET()
            .build();

        try {
            HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}
