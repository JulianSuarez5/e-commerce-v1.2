package ppi.e_commerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDto {
    private Integer id;
    private Integer userId;
    private String username;
    private String businessName;
    private String description;
    private String logoUrl;
    private String website;
    private String taxId;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Double rating;
    private Integer totalReviews;
    private Integer totalSales;
    private Double totalRevenue;
    private Boolean active;
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long productCount;

    public SellerDto(Integer id, String businessName, String description, String website, String taxId, String phone, String address, String city, String state, String zipCode, String country, String logo, Double rating, Boolean verified, Boolean active, LocalDateTime createdAt, Integer userId, String username, Long productCount) {
        this.id = id;
        this.businessName = businessName;
        this.description = description;
        this.website = website;
        this.taxId = taxId;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.logoUrl = logo;
        this.rating = rating;
        this.verified = verified;
        this.active = active;
        this.createdAt = createdAt;
        this.userId = userId;
        this.username = username;
        this.productCount = productCount;
    }
}
