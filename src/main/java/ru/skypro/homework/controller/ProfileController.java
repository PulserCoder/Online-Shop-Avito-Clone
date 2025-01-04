package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class ProfileController {
    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword) {
        if (newPassword != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else if (newPassword == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<HttpStatus> getDetailsAboutMe() {
        User me = new User();

        if (me != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUser> updateDetails(@RequestBody UpdateUser updateUser) {
        if (updateUser != null) {
            return ResponseEntity.ok(updateUser);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping(path = "/me/image", consumes = "multipart/form-data")
    public ResponseEntity<HttpStatus> updateImage(@RequestBody MultipartFile file) {
        if (file != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
