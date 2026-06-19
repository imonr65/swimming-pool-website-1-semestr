package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberShip {
    private Integer id;
    private UUID userId;
    private Integer workoutId;
    private LocalDate start_date;
    private LocalDate end_date;
    private Integer total_visit;
    private Integer current_amount_visit;
    private Pricing pricing;

    public String getFormattedStartDate() {
        return start_date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getFormattedEndDate() {
        return end_date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public Integer getRemainingVisits() {
        return total_visit - current_amount_visit;
    }

    public Double getUsagePercentage() {
        return (current_amount_visit * 100.0) / total_visit;
    }

    public boolean isActive() {
        return current_amount_visit < total_visit &&
                !end_date.isBefore(LocalDate.now());
    }
}
