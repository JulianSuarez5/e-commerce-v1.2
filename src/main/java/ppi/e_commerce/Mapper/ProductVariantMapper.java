package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ppi.e_commerce.Dto.ProductVariantDto;
import ppi.e_commerce.Model.ProductVariant;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(source = "product.id", target = "productId")
    ProductVariantDto toDto(ProductVariant variant);

    @Mapping(target = "product", ignore = true)
    void updateEntityFromDto(ProductVariantDto dto, @MappingTarget ProductVariant entity);
}
