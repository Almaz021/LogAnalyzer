import backend.academy.entities.LogRecord;
import backend.academy.entities.LogReport;
import backend.academy.parsers.LogParser;
import backend.academy.readers.FileReader;
import backend.academy.readers.URLReader;
import backend.academy.services.DataProcessorService;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import backend.academy.services.FilterService;
import backend.academy.writers.AdocFileWriter;
import backend.academy.writers.MarkdownFileWriter;
import org.junit.jupiter.api.Test;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AllTests {

    @Test
    public void testReadFile() throws IOException {
        FileReader fileReader = new FileReader(new DataProcessorService(new LogReport()));

        Stream<String> stream = fileReader.read("/resources/sample.txt");
        List<String> correct = new ArrayList<>();
        correct.add("Hello, World!");
        assertEquals(correct, stream.toList());
    }

    @Test
    public void testReadURL() throws IOException {
        URLReader urlReader = new URLReader();
        Stream<String> stream = urlReader.read(
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs");

        List<String> lines = stream.limit(1).toList();

        String correct =
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";
        assertEquals(correct, lines.getFirst());
    }

    @Test
    public void testParser() {
        LogParser parser = new LogParser();
        LogRecord logRecord = parser.parse(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");

        assertEquals("93.180.71.3", logRecord.remoteAddr());
        assertEquals("-", logRecord.remoteUser());
        assertEquals("2015-05-17", logRecord.timeLocal().toString());
        assertEquals("[\"GET, /downloads/product_1, HTTP/1.1\"]", Arrays.toString(logRecord.request()));
        assertEquals(304, logRecord.status());
        assertEquals(0, logRecord.bodyBytesSent().intValue());
        assertEquals("\"-\"", logRecord.httpReferer());
        assertEquals("\"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"", logRecord.httpUserAgent());
    }


    @Test
    public void testFilter() {
        LogRecord logRecord = new LogRecord(
            "",
            "",
            LocalDate.parse("2015-05-17", ISO_LOCAL_DATE),
            new String[] {},
            404,
            new BigDecimal(100),
            "",
            ""
        );
        LocalDate one = LocalDate.parse("2035-05-17", ISO_LOCAL_DATE);
        LocalDate two = LocalDate.parse("2014-05-17", ISO_LOCAL_DATE);

        FilterService filterService = new FilterService();
        boolean result1 = filterService.filterByDate(logRecord, two, one);
        boolean result2 = filterService.filterByDate(logRecord, two, "from");
        boolean result3 = filterService.filterByDate(logRecord, one, "to");

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }

    @Test
    public void testStatistics() {
        LogReport logReport = new LogReport();
        LogParser parser = new LogParser();
        DataProcessorService dataProcessorService = new DataProcessorService(logReport);
        LogRecord logRecord = parser.parse(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");
        List<BigDecimal> correctList = new ArrayList<>();
        Map<String, Integer> correctResourcesCount = new HashMap<>();
        Map<String, Integer> correctRequestStatusCount = new HashMap<>();
        correctResourcesCount.put("/downloads/product_1", 1);
        correctList.add(BigDecimal.ZERO);
        correctRequestStatusCount.put("304", 1);

        dataProcessorService.updateReport(logRecord);

        assertEquals(new ArrayList<>(), logReport.files());
        assertEquals("2015-05-17", logReport.startDate().toString());
        assertEquals("2015-05-17", logReport.endDate().toString());
        assertEquals(1, logReport.requestCount());
        assertEquals(0, logReport.sumRequestSize().intValue());
        assertEquals(correctList, logReport.allRequestSize());
        assertEquals(correctResourcesCount, logReport.resourcesCount());
        assertEquals(correctRequestStatusCount, logReport.requestStatusCount());
    }

    @Test
    public void testOutputAdoc() throws IOException {
        AdocFileWriter adocFileWriter = new AdocFileWriter();
        LogReport logReport = new LogReport();
        LogParser parser = new LogParser();
        DataProcessorService dataProcessorService = new DataProcessorService(logReport);

        LogRecord logRecord = parser.parse(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");
        dataProcessorService.updateReport(logRecord);

        String outputPath = "src/main/result.adoc";

        String correct = """
            == Общая информация
            [options="header"]
            |===
            |Метрика|Значение
            |Файл(-ы)|[]
            |Начальная дата|2015-05-17
            |Конечная дата|2015-05-17
            |Количество запросов|1
            |Средний размер ответа|0
            |95p размера ответа|0.0
            |===
            == Запрашиваемые ресурсы
            [options="header"]
            |===
            |Ресурс|Количество
            |/downloads/product_1|1
            |===
            == Коды ответа
            [options="header"]
            |===
            |Код|Имя|Количество
            |304|Not Modified|1
            |===
            == Типы запросов
            [options="header"]
            |===
            |Тип|Количество
            |"GET|1
            |===
            """;

        adocFileWriter.writeFile(outputPath, logReport);
        List<String> generatedLines = Files.readAllLines(Path.of(outputPath));
        List<String> expectedLines = correct.lines().toList();

        assertEquals(expectedLines.size(), generatedLines.size(), "Line counts do not match");

        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals(expectedLines.get(i), generatedLines.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testOutputMarkdown() throws IOException {
        MarkdownFileWriter adocFileWriter = new MarkdownFileWriter();
        LogReport logReport = new LogReport();
        LogParser parser = new LogParser();
        DataProcessorService dataProcessorService = new DataProcessorService(logReport);

        LogRecord logRecord = parser.parse(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");
        dataProcessorService.updateReport(logRecord);

        String outputPath = "src/main/result.md";

        String correct = """
            #### Общая информация
            |Метрика|Значение|
            |:---------------------:|-------------:|
            |Файл(-ы)|[]|
            |Начальная дата|2015-05-17|
            |Конечная дата|2015-05-17|
            |Количество запросов|1|
            |Средний размер ответа|0|
            |95p размера ответа|0.0|
            #### Запрашиваемые ресурсы
            |Ресурс|Количество|
            |:---------------------:|-------------:|
            |/downloads/product_1|1|
            #### Коды ответа
            |Код|Имя|Количество|
            |:---:|:---------------------:|-----------:|
            |304|Not Modified|1|
            #### Типы запросов
            |Тип|Количество|
            |:---------------------:|-------------:|
            |"GET|1|
            """;

        adocFileWriter.writeFile(outputPath, logReport);
        List<String> generatedLines = Files.readAllLines(Path.of(outputPath));
        List<String> expectedLines = correct.lines().toList();
        assertEquals(expectedLines.size(), generatedLines.size(), "Line counts do not match");

        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals(expectedLines.get(i), generatedLines.get(i), "Mismatch at line " + (i + 1));
        }
    }
}
