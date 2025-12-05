package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ppi.e_commerce.Dto.SellerDto;
import ppi.e_commerce.Model.Seller;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    
    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "productCount", ignore = true)
    SellerDto toDto(Seller seller);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "products", ignore = true)
    Seller toEntity(SellerDto dto);
}

