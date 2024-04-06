package ru.sejapoe.javapr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.sejapoe.javapr.domain.UserEntity;
import ru.sejapoe.javapr.dto.users.CreateUserDto;
import ru.sejapoe.javapr.dto.users.UserDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(CreateUserDto createUserDto);
}