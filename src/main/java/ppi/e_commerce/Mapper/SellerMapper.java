package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ppi.e_commerce.Dto.CreateSellerRequest;
import ppi.e_commerce.Dto.SellerDto;
import ppi.e_commerce.Model.Seller;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "productCount", ignore = true)
    SellerDto toDto(Seller seller);

    List<SellerDto> toDtoList(List<Seller> sellers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CreateSellerRequest request, @MappingTarget Seller seller);

}
