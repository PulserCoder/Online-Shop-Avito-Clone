package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.models.UserEntity;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    UserEntity registerToUserEntity(Register register);
    Register userEntityToRegister(UserEntity userEntity);
}
