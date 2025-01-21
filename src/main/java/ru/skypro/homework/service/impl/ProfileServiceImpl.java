package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
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
import ru.skypro.homework.service.S3Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final S3Service s3Service;

    @Value("${s3.selectel.domain}")
    private String domain;

    public ProfileServiceImpl(UserRepository userRepository,
                              PasswordEncoder encoder,
                              UserMapper userMapper,
                              S3Service s3Service) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.s3Service = s3Service;
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
    public void changeImage(MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        if (userEntity.isEmpty()) {
            return;
        }

        UserEntity user = userEntity.get();

        String fileName = user.getId() + "-Avatar";
        if (s3Service.uploadFile(file, fileName)) {
            fileName = domain + "/" + fileName + "." + s3Service.getExtension(file.getOriginalFilename());

            user.setImage(fileName);

            userRepository.save(user);
        }
    }

    @Override
    public Optional<UserEntity> getUserById(long id) {
        return userRepository.findById(id);
    }
}
