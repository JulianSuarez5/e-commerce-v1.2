package ppi.e_commerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ppi.e_commerce.Dto.UserDto;
import ppi.e_commerce.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);
    
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tempPasswordHash", ignore = true)
    @Mapping(target = "usingTempPassword", ignore = true)
    @Mapping(target = "tempPasswordExpiry", ignore = true)
    @Mapping(target = "products", ignore = true)
    User toEntity(UserDto dto);
}

