package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.models.UserEntity;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    @Mapping(source = "username", target = "email")
    @Mapping(target = "id", ignore = true)
    UserEntity registerToUserEntity(Register register);

    @Mapping(source = "email", target = "username")
    Register userEntityToRegister(UserEntity userEntity);
}
