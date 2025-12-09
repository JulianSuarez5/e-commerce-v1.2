package ppi.e_commerce.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderTrackingDto {
    private Integer id;
    private String status;
    private String description;
    private String location;
    private String trackingNumber;
    private LocalDateTime timestamp;
}
