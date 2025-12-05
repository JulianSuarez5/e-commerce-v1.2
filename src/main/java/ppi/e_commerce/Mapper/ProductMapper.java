package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ppi.e_commerce.Dto.ProductDto;
import ppi.e_commerce.Model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "seller.id", target = "sellerId")
    ProductDto toDto(Product product);

    @Mapping(source = "categoryName", target = "category.name")
    @Mapping(source = "brandName", target = "brand.name")
    @Mapping(source = "sellerId", target = "seller.id")
    Product toEntity(ProductDto productDto);
}
