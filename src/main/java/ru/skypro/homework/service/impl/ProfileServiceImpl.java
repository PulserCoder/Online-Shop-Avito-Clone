package ru.skypro.homework.service.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
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
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private static final String UPLOAD_DIR = "uploads/";

    public ProfileServiceImpl(UserRepository userRepository,
                              UserDetailsManager manager,
                              PasswordEncoder encoder,
                              UserMapper userMapper) {
        this.userRepository = userRepository;
        this.manager = manager;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity =  userRepository.findByEmail(username);

        if (userEntity == null) {
            return null;
        }

        return userMapper.userEntityToUser(userEntity);
    }

    @Override
    public void updateUser(UpdateUser updateUser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username);

        userEntity.setFirstName(updateUser.getFirstName());
        userEntity.setLastName(updateUser.getLastName());
        userEntity.setPhone(updateUser.getPhone());

        userRepository.save(userEntity);
    }

    @Override
    public boolean changePassword(NewPassword newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username);

        if (!encoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            return false;
        }

        userEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(userEntity);


        UserDetails updatedUser = org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getEmail())
                .password(encoder.encode(newPassword.getNewPassword()))
                .roles(userEntity.getRole().name())
                .build();

        manager.updateUser(updatedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                updatedUser,
                updatedUser.getPassword(),
                updatedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }

    @Override
    public void changeImage(MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username);

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

        userEntity.setImage(fileName);
    }

    @Override
    public Optional<UserEntity> getUserById(long id) {
        return userRepository.findById(id);
    }
}
