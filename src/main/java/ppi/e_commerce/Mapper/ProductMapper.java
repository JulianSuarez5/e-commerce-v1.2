package ppi.e_commerce.Mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Exception.ResourceNotFoundException;
import ppi.e_commerce.Model.Brand;
import ppi.e_commerce.Model.Category;
import ppi.e_commerce.Model.Product;
import ppi.e_commerce.Model.Seller;
import ppi.e_commerce.Repository.BrandRepository;
import ppi.e_commerce.Repository.CategoryRepository;
import ppi.e_commerce.Repository.SellerRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "seller.businessName", target = "sellerName")
    @Mapping(target = "imageUrls", ignore = true) // Custom logic needed for media mapping
    @Mapping(target = "model3dUrls", ignore = true) // Custom logic needed for media mapping
    public abstract ProductDto toDto(Product product);

    public abstract List<ProductDto> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "productMedia", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    public abstract void updateEntityFromDto(ProductDto dto, @MappingTarget Product entity);

    @AfterMapping
    protected void afterUpdateEntityFromDto(ProductDto dto, @MappingTarget Product entity) {
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));
            entity.setCategory(category);
        }
        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + dto.getBrandId()));
            entity.setBrand(brand);
        }
        if (dto.getSellerId() != null) {
            Seller seller = sellerRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + dto.getSellerId()));
            entity.setSeller(seller);
        }
    }
}
