package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.profile.NewPassword;
import ru.skypro.homework.dto.profile.UpdateUser;
import ru.skypro.homework.dto.profile.User;
import ru.skypro.homework.service.ProfileService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "API для управления пользовательскими профилями")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "Изменить пароль пользователя", description = "Позволяет пользователю изменить свой пароль")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль успешно изменён"),
            @ApiResponse(responseCode = "403", description = "Запрос отклонён, неверные данные для смены пароля"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword) {
        if (profileService.changePassword(newPassword)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Получить информацию о пользователе",
            description = "Возвращает данные о текущем авторизованном пользователе. Возвращает ошибку 404, если пользователь не найден.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    @GetMapping(path = "/me")
    public ResponseEntity<User> getDetailsAboutMe() {
        try {
            return ResponseEntity.ok(profileService.getCurrentUser());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Обновить данные пользователя", description = "Обновляет информацию о текущем авторизованном пользователе")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены"),
            @ApiResponse(responseCode = "400", description = "Ошибка при обновлении данных"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUser> updateDetails(@RequestBody UpdateUser updateUser) {
        try {
            profileService.updateUser(updateUser);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Обновить изображение профиля пользователя",
            description = "Загружает и обновляет изображение профиля текущего авторизованного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
            @ApiResponse(responseCode = "400", description = "Ошибка загрузки изображения"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
    })
    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> updateImage(@RequestPart("image") MultipartFile file) {
        try {
            profileService.changeImage(file);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
