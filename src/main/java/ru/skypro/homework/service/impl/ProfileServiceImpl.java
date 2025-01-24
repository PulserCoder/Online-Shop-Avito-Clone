package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final FileUploaderImpl fileUploader;

    public ProfileServiceImpl(UserRepository userRepository,
                              PasswordEncoder encoder,
                              UserMapper userMapper,
                              FileUploaderImpl fileUploader) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.fileUploader = fileUploader;
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        return userEntity
                .map(userMapper::userEntityToUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
    }
    @Override
    public void updateUser(UpdateUser updateUser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        if (userEntity.isEmpty()) {
            throw new RuntimeException("User with email " + username + " not found");
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
            throw new RuntimeException("User with email " + username + " not found");
        }

        UserEntity user = userEntity.get();

        String fileName = user.getId() + "-Avatar";
        try {
            if (fileUploader.saveFile(file, fileName)) {
                fileName = "/file/" + fileName + "." + fileUploader.getExtension(Objects.requireNonNull(file.getOriginalFilename()));

                user.setImage(fileName);

                userRepository.save(user);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<UserEntity> getUserById(long id) {
        return userRepository.findById(id);
    }
}
