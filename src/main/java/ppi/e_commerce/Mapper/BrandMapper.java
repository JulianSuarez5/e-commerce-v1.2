package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ppi.e_commerce.Dto.BrandDto;
import ppi.e_commerce.Model.Brand;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    BrandDto toDto(Brand brand);

    Brand toEntity(BrandDto brandDto);
}
