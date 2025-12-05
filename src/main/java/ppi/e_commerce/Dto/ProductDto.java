package ppi.e_commerce.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private boolean active;
    private String sku;

    private Integer categoryId;
    private String categoryName;

    private Integer brandId;
    private String brandName;

    private Integer sellerId;
    private String sellerName;

    private List<String> imageUrls;
    private List<String> model3dUrls;

    public ProductDto(Integer id, String name, String description, Double price, int stock, boolean active, String sku,
                      Integer categoryId, String categoryName, Integer brandId, String brandName, Integer sellerId, String sellerName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.active = active;
        this.sku = sku;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.brandId = brandId;
        this.brandName = brandName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }
}
