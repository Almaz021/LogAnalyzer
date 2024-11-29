package backend.academy.settings;

import lombok.experimental.UtilityClass;

/**
 * Utility class that holds constant strings used throughout the application for formatting
 * and generating report content in different formats (Markdown, AsciiDoc).
 * <p>
 * These constants are primarily used for report headers, table formatting, and metrics representation.
 */
@UtilityClass
public class ConstantStrings {
    /* General formatting constants */
    public static final String NEW_LINE = "\n";
    public static final String TABLE_ROW_STRING_INT = "|%s|%d";
    public static final String RESPONSE_CODES_TABLE_ROW = "|%s|%s|%d";

    /* Report headers */
    public static final String GENERAL_INFO_HEADER = "Общая информация%s";
    public static final String RESOURCE_HEADER = "Запрашиваемые ресурсы%s";
    public static final String RESPONSE_CODES_HEADER = "Коды ответа%s";
    public static final String REQUEST_TYPES_HEADER = "Типы запросов%s";

    /* Table headers for various metrics */
    public static final String METRIC_TABLE_HEADER = "|Метрика|Значение";
    public static final String RESOURCE_TABLE_HEADER = "|Ресурс|Количество";
    public static final String RESPONSE_CODES_TABLE_HEADER = "|Код|Имя|Количество";
    public static final String REQUEST_TYPES_TABLE_HEADER = "|Тип|Количество";

    /* Metric representation for general report */
    public static final String FILES_METRIC = "|Файл(-ы)|%s";
    public static final String START_DATE_METRIC = "|Начальная дата|%s";
    public static final String END_DATE_METRIC = "|Конечная дата|%s";
    public static final String REQUEST_COUNT_METRIC = "|Количество запросов|%d";
    public static final String AVG_RESPONSE_SIZE_METRIC = "|Средний размер ответа|%s";
    public static final String PERCENTILE_RESPONSE_SIZE_METRIC = "|95p размера ответа|%s";

    /* Markdown formatting constants */
    public static final String HEADER_MD = "#### %s";
    public static final String TABLE_ROW_MD = "%s|%s";
    public static final String TABLE_ALIGNMENT_MD = "|:---------------------:|-------------:|\n";
    public static final String TABLE_ALIGNMENT_MD_DETAILED = "|:---:|:---------------------:|-----------:|\n";

    /* AsciiDoc formatting constants */
    public static final String HEADER_ADOC = "== %s";
    public static final String TABLE_ROW_ADOC = "%s%S";
    public static final String TABLE_ADOC = "|===%s";

    /* AsciiDoc table headers with options */
    public static final String METRIC_TABLE_HEADER_ADOC = "[options=\"header\"]%s|===%s|Метрика|Значение%s";
    public static final String RESOURCE_TABLE_HEADER_ADOC = "[options=\"header\"]%s|===%s|Ресурс|Количество%s";
    public static final String RESPONSE_CODES_TABLE_HEADER_ADOC = "[options=\"header\"]%s|===%s|Код|Имя|Количество%s";
    public static final String REQUEST_TYPES_TABLE_HEADER_ADOC = "[options=\"header\"]%s|===%s|Тип|Количество%s";
}
