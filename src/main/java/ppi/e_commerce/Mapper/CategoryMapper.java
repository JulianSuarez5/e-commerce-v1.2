package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ppi.e_commerce.Dto.CategoryDto;
import ppi.e_commerce.Model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mappings({
        @Mapping(target = "productCount", ignore = true) // Ignored: will be set manually
    })
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
