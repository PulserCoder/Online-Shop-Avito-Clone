package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.RegisterMapper;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

/**
 * Реализация сервиса аутентификации и регистрации пользователей.
 * <p>
 * Этот сервис предоставляет функциональность для логина и регистрации пользователей.
 * Для аутентификации используется пароль, который проверяется с помощью {@link PasswordEncoder}.
 * Регистрация нового пользователя проверяет уникальность email и сохраняет пользователя в базе данных.
 * </p>
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RegisterMapper registerMapper;

    /**
     * Конструктор для инициализации всех зависимостей.
     *
     * @param passwordEncoder компонент для шифрования паролей
     * @param userRepository репозиторий для работы с пользователями в базе данных
     * @param registerMapper маппер для преобразования данных регистрации в сущность пользователя
     */
    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           RegisterMapper registerMapper) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.registerMapper = registerMapper;
    }

    /**
     * Метод для аутентификации пользователя.
     * <p>
     * Проверяет, существует ли пользователь с указанным email и совпадает ли указанный пароль
     * с хранимым паролем в базе данных.
     * </p>
     *
     * @param userName email пользователя для аутентификации
     * @param password пароль пользователя
     * @return {@code true}, если пользователь найден и пароль совпадает, иначе {@code false}
     */
    @Override
    public boolean login(String userName, String password) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(userName);

        if (userEntity.isEmpty()) {
            return false;
        }

        return userEntity.filter(entity -> encoder.matches(password, entity.getPassword())).isPresent();
    }

    /**
     * Метод для регистрации нового пользователя.
     * <p>
     * Проверяет, существует ли уже пользователь с таким email. Если пользователь не существует,
     * то создается новый пользователь с зашифрованным паролем и сохраняется в базе данных.
     * </p>
     *
     * @param register объект данных регистрации, содержащий email, пароль и другие данные пользователя
     * @return {@code true}, если регистрация прошла успешно, иначе {@code false}
     */
    @Override
    public boolean register(Register register) {
        if (register == null) {
            return false;
        }

        if (userRepository.existsByEmail(register.getUsername())) {
            return false;
        }

        UserEntity user = registerMapper.registerToUserEntity(register);

        user.setPassword(encoder.encode(register.getPassword()));

        userRepository.save(user);

        return true;
    }
}
