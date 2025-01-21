package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.S3Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("s3")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping(value = "/{name}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> addFile(
            @PathVariable String name,
            @RequestParam("file") MultipartFile file) {
        return s3Service.uploadFile(file, name) ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteFile(@RequestParam String link) {
        System.out.println(link);
        return s3Service.deleteFile(link) ? new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
