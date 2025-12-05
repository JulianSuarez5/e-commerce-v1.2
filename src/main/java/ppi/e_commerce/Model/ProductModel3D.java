package ppi.e_commerce.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_models_3d")
public class ProductModel3D {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String url; // URL del archivo GLB/GLTF

    private String format; // GLB, GLTF
    private Long fileSize; // bytes
    private String thumbnailUrl; // Preview image
    private Boolean optimized = false; // Si está comprimido con DRACO
    private Boolean isPrimary = true;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public ProductModel3D() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public Boolean getOptimized() { return optimized; }
    public void setOptimized(Boolean optimized) { this.optimized = optimized; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

