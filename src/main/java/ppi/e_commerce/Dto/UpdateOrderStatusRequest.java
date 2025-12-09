package ppi.e_commerce.Dto;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private String status;
    private String description;
    private String location;
    private String trackingNumber;
}
