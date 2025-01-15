package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.profile.NewPassword;
import ru.skypro.homework.dto.profile.UpdateUser;
import ru.skypro.homework.dto.profile.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ProfileService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private static final String UPLOAD_DIR = "uploads/";

    public ProfileServiceImpl(UserRepository userRepository,
                              PasswordEncoder encoder,
                              UserMapper userMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity =  userRepository.findByEmail(username);

        return userEntity.map(userMapper::userEntityToUser).orElse(null);

    }

    @Override
    public void updateUser(UpdateUser updateUser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        if (userEntity.isEmpty()) {
            return;
        }

        UserEntity user = userEntity.get();

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());

        userRepository.save(user);

    }

    @Override
    public boolean changePassword(NewPassword newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        if (userEntity.isEmpty()) {
            return false;
        }

        UserEntity user = userEntity.get();

        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);

        return true;
    }

    @Override
    public void changeImage(MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        if (userEntity.isEmpty()) {
            return;
        }

        UserEntity user = userEntity.get();

        Path uploadDir = Path.of(UPLOAD_DIR);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }


        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            return;
        }

        Path filePath = uploadDir.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        user.setImage(fileName);
    }
}
