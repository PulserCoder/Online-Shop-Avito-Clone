package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.profile.NewPassword;
import ru.skypro.homework.dto.profile.UpdateUser;
import ru.skypro.homework.dto.profile.User;
import ru.skypro.homework.service.ProfileService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword) {
        if (profileService.changePassword(newPassword)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(path = "/me")
    public ResponseEntity<User> getDetailsAboutMe() {
        return ResponseEntity.ok(profileService.getCurrentUser());
    }

    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUser> updateDetails(@RequestBody UpdateUser updateUser) {
        profileService.updateUser(updateUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(path = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<HttpStatus> updateImage(@RequestBody MultipartFile file) throws IOException {
            profileService.changeImage(file);

            return ResponseEntity.status(HttpStatus.OK).build();
    }
}
