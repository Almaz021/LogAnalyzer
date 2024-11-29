package backend.academy.settings;

import lombok.experimental.UtilityClass;

/**
 * Utility class that holds constant configuration settings used throughout the application.
 * <p>
 * These settings include file paths for result reports, percentile values, and common integer constants.
 */
@UtilityClass
public class Settings {
    public static final int PERCENTILE_95 = 95;
    public static final String USER_HOME_DIRECTORY = System.getProperty("user.home");
    public static final String RESULT_MARKDOWN_PATH = USER_HOME_DIRECTORY + "/result.md";
    public static final String RESULT_ADOC_PATH = USER_HOME_DIRECTORY + "/result.adoc";
    public static final int ZERO = 0;
    public static final int ONE = 1;
}
