package backend.academy.readers;

import backend.academy.interfaces.LogReader;
import backend.academy.services.DataProcessorService;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileReader implements LogReader {
    private final DataProcessorService dataProcessorService;

    @Override
    public Stream<String> read(String filePath) throws IOException {

        StringBuilder basePath = new StringBuilder();
        StringBuilder currLine = new StringBuilder();
        int currIndex = 0;

        while (currIndex < filePath.length() && filePath.charAt(currIndex) != '*') {
            if (filePath.charAt(currIndex) == '/') {
                basePath.append(currLine).append('/');
                currLine = new StringBuilder();
            } else {
                currLine.append(filePath.charAt(currIndex));
            }
            currIndex++;
        }

        String globPattern = "glob:";

        if (currIndex == filePath.length()) {
            basePath.append(currLine);
            globPattern += "**/*.txt";
        } else {
            globPattern += "**/" + currLine + filePath.substring(currIndex);
            if (!globPattern.endsWith(".txt")) {
                globPattern += "/*.txt";
            }
        }

        Path path = Paths.get("src/main/" + basePath);

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(globPattern);
        try (Stream<Path> stream = Files.walk(path)) {
            List<Path> foundFiles = stream
                .filter(Files::isRegularFile)
                .filter(matcher::matches)
                .toList();
            foundFiles.forEach(o -> {
                dataProcessorService.addFileName(o.toString());
            });
            return foundFiles.stream()
                .flatMap(pathE -> {
                    try {
                        return Files.lines(pathE);
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                });
        }
    }
}