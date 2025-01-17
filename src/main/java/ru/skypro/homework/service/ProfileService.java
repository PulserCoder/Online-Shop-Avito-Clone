package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.profile.NewPassword;
import ru.skypro.homework.dto.profile.UpdateUser;
import ru.skypro.homework.dto.profile.User;
import ru.skypro.homework.models.UserEntity;

import java.io.IOException;
import java.util.Optional;

public interface ProfileService {
    boolean changePassword(NewPassword newPassword);
    void updateUser(UpdateUser updateUser);
    User getCurrentUser();
    void changeImage(MultipartFile file) throws IOException;

    Optional<UserEntity> getUserById(long id);
}
