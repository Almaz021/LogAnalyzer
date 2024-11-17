package backend.academy.entities;

import com.google.common.math.Quantiles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import static backend.academy.settings.Settings.PERCENTILE_95;

@Getter
@Setter
public class LogReport {
    private List<String> files = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer requestCount = 0;
    private BigDecimal sumRequestSize = BigDecimal.ZERO;
    private List<BigDecimal> allRequestSize = new ArrayList<>();
    private Map<String, Integer> resourcesCount = new HashMap<>();
    private Map<String, Integer> requestStatusCount = new HashMap<>();
    private Map<String, Integer> requestTypeCount = new HashMap<>();

    public BigDecimal getAverageRequestSize() {
        return sumRequestSize.divide(new BigDecimal(requestCount), RoundingMode.FLOOR);
    }

    public Double getPercentile() {
        return Quantiles.percentiles().index(PERCENTILE_95).compute(allRequestSize);
    }
}
