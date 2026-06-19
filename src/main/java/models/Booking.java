package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private Integer id;
    private UUID userId;
    private Integer scheduleId;
    private Integer membershipId;
    private Integer priceId;
    private User user;
}
